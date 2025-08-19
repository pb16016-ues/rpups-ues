from selenium import webdriver
from selenium.webdriver.edge.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait, Select
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException, StaleElementReferenceException, NoSuchElementException
import time
import pandas as pd
from datetime import datetime
import re
from openpyxl import load_workbook

#webDriver
ruta_driver = 'msedgedriver.exe'
servicio = Service(executable_path=ruta_driver)
opciones = webdriver.EdgeOptions()
opciones.add_argument("start-maximized")
navegador = webdriver.Edge(service=servicio, options=opciones)


#crear los DataFrame
data = {
    'Country': [],
    'Theater': [],
    'Date': [],
    'Time': [],
    'Movie': [],
    'Format': [] 
}

otros_data = {
    'Country': [],
    'Theater': [],
    'Date': [],
    'Time': [],
    'Movie': [],
    'Format': [] 
}

MESES_ES = {
    'enero': 1, 'febrero': 2, 'marzo': 3, 'abril': 4, 'mayo': 5, 'junio': 6,
    'julio': 7, 'agosto': 8, 'septiembre': 9, 'setiembre': 9, 'octubre': 10,
    'noviembre': 11, 'diciembre': 12
}


#función para obtener la fecha actual en el formato deseado
def get_current_date():
    return datetime.now().strftime("%d-%m-%Y")


#función para determinar el país basado en la extensión de la URL
def determine_country(url):
    if ".sv" in url:
        return "El Salvador"
    elif ".hn" in url:
        return "Honduras"
    elif ".gt" in url:
        return "Guatemala"
    elif ".cr" in url:
        return "Costa Rica"
    elif ".pa" in url:
        return "Panamá"
    else:
        return "Desconocido"
    

def a_dd_mm_yyyy_desde_iso(iso_date_str: str) -> str:
    y, m, d = iso_date_str.split('-')
    return f"{d.zfill(2)}-{m.zfill(2)}-{y}"


def a_dd_mm_yyyy_desde_texto_pa(txt: str) -> str:
    txt = (txt or "").strip().lower()
    m = re.search(r"\((\d{1,2})\s+([a-záéíóúñ]+)\)", txt)
    if not m:
        m = re.search(r"(\d{1,2})\s+([a-záéíóúñ]+)", txt)

    if not m:
        return datetime.now().strftime("%d-%m-%Y")

    dia = int(m.group(1))
    mes_nombre = m.group(2)
    now = datetime.now()
    mes = MESES_ES.get(mes_nombre, 0)
    if mes == 0:
        return now.strftime("%d-%m-%Y")
    year = now.year
    if mes < now.month or (mes == now.month and dia < now.day):
        year += 1
    return f"{dia:02d}-{mes:02d}-{year}"
    

def esperar_y_encontrar_elemento(por, valor, tiempo_espera=6):
    try:
        return WebDriverWait(navegador, tiempo_espera).until(
            EC.presence_of_element_located((por, valor))
        )
    except TimeoutException:
        #print(f"Elemento {valor} no encontrado.")
        return None


#función para esperar a que un elemento sea clicable y hacer clic
def esperar_y_hacer_click(por, valor, tiempo_espera=6):
    try:
        elemento = WebDriverWait(navegador, tiempo_espera).until(
            EC.element_to_be_clickable((por, valor))
        )
        navegador.execute_script("arguments[0].scrollIntoView(true);", elemento)
        elemento.click()
    except TimeoutException:
        print(f"El elemento {valor} no estuvo disponible para hacer clic.")
    except StaleElementReferenceException:
        print(f"El elemento {valor} ya no es accesible en el DOM.")


#función para cerrar el popup si aparece
def cerrar_popup(tiempo_espera=5):
    try:
        boton_cerrar_popup = WebDriverWait(navegador, tiempo_espera).until(
            EC.element_to_be_clickable((By.ID, 'takeover-close'))
        )
        boton_cerrar_popup.click()
        print("Popup cerrado.")
    except TimeoutException:
        print("No apareció ningún popup.")



#función para extraer la cartelera de todos los cines de cada país
def extraer_cartelera_todos_cines_react():
    try:
        popup_btn = esperar_y_encontrar_elemento(By.CLASS_NAME, 'show-cinemas-popup', tiempo_espera=5)
        if not popup_btn:
            print("No se encontró el botón para abrir el popup de cines.")
            return
        navegador.execute_script("arguments[0].click();", popup_btn)
        time.sleep(0.5)

        contenedor_cines = esperar_y_encontrar_elemento(By.CLASS_NAME, 'Cinemas_sectionCinemas__7v0LB', tiempo_espera=8)
        if not contenedor_cines:
            print("No se encontró el contenedor de cines.")
            return

        anchors = contenedor_cines.find_elements(By.CSS_SELECTOR, 'div.Cinema_cinema__3mgID > a')
        cinemas = []
        for a in anchors:
            try:
                site_id = a.get_attribute('data-site-id') or ''
                name = (a.get_attribute('data-site-name') or a.find_element(By.TAG_NAME, 'h4').text).strip()
                href = a.get_attribute('href') or ''
                if site_id.strip() in ('', '0') or name.lower().strip() in ('todo', 'all') or href.endswith('/cinema/all/'):
                    continue
                cinemas.append({'site_id': site_id, 'name': name, 'href': href})
            except Exception:
                continue

        print(f"Se encontraron {len(cinemas)} cines válidos para procesar.")

        for cine in cinemas:
            try:
                #reabrir popup y seleccionar el cine en el elemento data-site-id
                popup_btn = esperar_y_encontrar_elemento(By.CLASS_NAME, 'show-cinemas-popup', tiempo_espera=5)
                if not popup_btn:
                    print("No se pudo reabrir el popup para seleccionar el cine", cine['name'])
                    continue
                navegador.execute_script("arguments[0].click();", popup_btn)
                time.sleep(0.5)

                contenedor_cines = esperar_y_encontrar_elemento(By.CLASS_NAME, 'Cinemas_sectionCinemas__7v0LB', tiempo_espera=5)
                if not contenedor_cines:
                    print("Popup abierto pero no cargó la lista de cines para", cine['name'])
                    continue

                selector = f"a[data-site-id='{cine['site_id']}']"
                anchor = contenedor_cines.find_element(By.CSS_SELECTOR, selector)
                navegador.execute_script("arguments[0].scrollIntoView(true);", anchor)
                navegador.execute_script("arguments[0].click();", anchor)

                cargado = False
                for _ in range(15):
                    if esperar_y_encontrar_elemento(By.CLASS_NAME, 'movie-projections', tiempo_espera=3) or \
                       esperar_y_encontrar_elemento(By.CLASS_NAME, 'Movies_movies__3S7t4', tiempo_espera=3):
                        cargado = True
                        break
                    time.sleep(0.5)

                if not cargado:
                    print(f"No se cargó la cartelera para {cine['name']}.")
                    with open(f"debug_no_cartelera_{cine['site_id']}.html", "w", encoding="utf-8") as f:
                        f.write(navegador.page_source)
                    continue

                print(f"Procesando cartelera del cine: {cine['name']}")

                #HOY: Información de cartelera actual
                extraer_cartelera_cine_react(cine['name'])

                #OTROS HORARIOS (futuras fechas): Información de cartelera de próximas fechas
                extraer_fechas_siguientes_y_cartelera_react(cine['name'])

            except Exception as e:
                print(f"Error procesando cine {cine['name']}: {e}")
                continue
    except Exception as e:
        print(f"Error en extraer_cartelera_todos_cines: {e}")



#función para extraer la cartelera de cada cine específico de cada país
def extraer_cartelera_cine_react(nombre_cine, dataset=None, fecha_personalizada=None):
    if dataset is None:
        dataset = data
    fecha_val = fecha_personalizada or get_current_date()

    try:
        cont = esperar_y_encontrar_elemento(By.CLASS_NAME, 'movie-projections', tiempo_espera=5)
        if cont:
            projs = cont.find_elements(By.CLASS_NAME, 'movie-projection')
            for proj in projs:
                try:
                    inner = proj.find_element(By.CLASS_NAME, 'movie-projection__inner')
                    titulo = inner.find_element(By.TAG_NAME, 'h2').text.strip()
                    lis = proj.find_elements(By.XPATH, ".//ul/li")

                    if lis:
                        for li in lis:
                            try:
                                label = li.find_element(By.TAG_NAME, 'label')
                                hora = label.text.splitlines()[0].strip()
                                formato_idioma = ""

                                try:
                                    span = label.find_element(By.TAG_NAME, 'span')
                                    formato_idioma = (span.get_attribute('title') or span.text or "").strip()
                                except Exception:
                                    pass

                                items = [s.strip().upper() for s in formato_idioma.replace('/', ',').split(',') if s.strip()]
                                idioma = 'SUB' if any(x in items for x in ('SUBTITLE', 'SUB')) else ('DOB' if any(x in items for x in ('DOB', 'DOBLADA', 'DOBLADO')) else '')

                                formato = ""
                                for x in items:
                                    if x in ('2D', '3D', '4D', '4DX', 'IMAX', 'VIP', 'XE', 'JUNIOR'):
                                        if formato:
                                            formato += " "
                                        formato += x
                                if not formato and items:
                                    formato = items[0]

                                dataset['Country'].append(determine_country(navegador.current_url))
                                dataset['Theater'].append(nombre_cine)
                                dataset['Date'].append(fecha_val)
                                dataset['Time'].append(hora)
                                dataset['Movie'].append(titulo)
                                dataset['Format'].append(f"{formato} {idioma}")
                                
                            except Exception:
                                continue
                    else:
                        dataset['Country'].append(determine_country(navegador.current_url))
                        dataset['Theater'].append(nombre_cine)
                        dataset['Date'].append(fecha_val)
                        dataset['Time'].append('')
                        dataset['Movie'].append(titulo)
                        dataset['Format'].append('')

                except Exception:
                    continue
            return

        cont_react = esperar_y_encontrar_elemento(By.CLASS_NAME, 'Movies_movies__3S7t4', tiempo_espera=5)
        if cont_react:
            peliculas = cont_react.find_elements(By.CLASS_NAME, 'Movies_movie__14sNy')
            for pelicula in peliculas:
                try:
                    titulo = pelicula.find_element(By.TAG_NAME, 'h3').text.strip()
                    meta = ""
                    try:
                        meta = pelicula.find_element(By.CLASS_NAME, 'Movies_movieMeta__1i2K-').text.strip()
                    except Exception:
                        pass

                    dataset['Country'].append(determine_country(navegador.current_url))
                    dataset['Theater'].append(nombre_cine)
                    dataset['Date'].append(fecha_val)
                    dataset['Time'].append('')
                    dataset['Movie'].append(titulo)
                    dataset['Format'].append(meta)
                except Exception:
                    continue
            return
    except Exception as e:
        print(f"Error en extraer_cartelera_cine: {e}")


#Función para extraer fechas siguientes y sus respectivas carteleras de cada cine de cada país
def extraer_fechas_siguientes_y_cartelera_react(nombre_cine):
    try:
        fechas_ul = esperar_y_encontrar_elemento(By.CSS_SELECTOR, ".step__content .list-movie-dates", tiempo_espera=5)
        if not fechas_ul:
            return

        #elementos label que contienen las fechas futuras
        labels = navegador.find_elements(By.CSS_SELECTOR, ".step__content .list-movie-dates .slick-list .slick-slide label[for^='field-movie-date-']")
        if len(labels) <= 1:
            return

        #omite el primer label
        for idx, lbl in enumerate(labels):
            if idx == 0:
                continue
            date_for = lbl.get_attribute("for")
            fecha_iso = date_for.replace("field-movie-date-", "")
            fecha_texto = (lbl.text or fecha_iso).strip()

            try:
                navegador.execute_script("arguments[0].scrollIntoView({block:'center'});", lbl)
                navegador.execute_script("arguments[0].click();", lbl)
            except Exception:

                try:
                    next_btn = navegador.find_element(By.CSS_SELECTOR, ".step__content .list-movie-dates .slick-next")
                    navegador.execute_script("arguments[0].click();", next_btn)
                    time.sleep(0.3)
                    navegador.execute_script("arguments[0].click();", lbl)
                except Exception as e2:
                    print(f"No pude clicar fecha {fecha_texto}: {e2}")
                    continue

            try:
                esperar_y_encontrar_elemento(By.CSS_SELECTOR, f"#{date_for}:checked", tiempo_espera=4)
            except TimeoutException:
                time.sleep(0.5)

            try:
                esperar_y_encontrar_elemento(By.CSS_SELECTOR, ".movie-projections, .Movies_movies__3S7t4", tiempo_espera=4)
            except TimeoutException:
                pass
            time.sleep(0.5)

            fecha_personalizada = datetime.strptime(fecha_iso, "%Y-%m-%d").strftime("%d-%m-%Y")

            #Extraer funciones de esa fecha en el dataset 'otros_data'
            extraer_cartelera_cine_react(nombre_cine, dataset=otros_data, fecha_personalizada=fecha_personalizada)

    except Exception as e:
        print(f"Error al recorrer próximas fechas: {e}")



#Función para extraer información de las películas
def extraer_horarios_peliculas_pa_gt(flag=0, dataset=None, fecha_personalizada=None):
    global is_panama
    is_panama = ".pa" in url

    if dataset is None:
        dataset = data
    fecha_val = fecha_personalizada or get_current_date()

    if is_panama:
        lista_cines = esperar_y_encontrar_elemento(By.CLASS_NAME, 'listaCarteleraHorario')
        if not lista_cines:
            return
        
        if lista_cines:
            cines = lista_cines.find_elements(By.CLASS_NAME, 'divComplejo')
            for cine in cines:
                try:
                    nombre_cine_elemento = cine.find_element(By.TAG_NAME, 'h2')
                    nombre_cine = nombre_cine_elemento.text if nombre_cine_elemento else "Desconocido"
                    nombre_cine = nombre_cine.rstrip('?').strip()

                    if flag == 1:
                        print(f"Procesando cartelera del cine: {nombre_cine}")

                    fechas = cine.find_elements(By.CLASS_NAME, 'divFecha')
                    for fecha in fechas:
                        peliculas = fecha.find_elements(By.CLASS_NAME, 'tituloPelicula')
                        for pelicula in peliculas:
                            titulo = pelicula.find_element(By.CLASS_NAME, 'datalayer-movie').text
                            formatos = pelicula.find_elements(By.CLASS_NAME, 'horarioExp')
                            for formato in formatos:
                                tipo_formato = formato.find_element(By.CLASS_NAME, 'col3').text
                                formato_pelicula = ""

                                try:
                                    imagen_3d = formato.find_element(By.XPATH, ".//img[contains(@src, '3d.png')]")
                                    if imagen_3d:
                                        formato_pelicula = "3D"
                                except NoSuchElementException:
                                    pass
                                
                                try:
                                    imagen_4d = formato.find_element(By.XPATH, ".//img[contains(@src, '4d.png')]")
                                    if imagen_4d:
                                        formato_pelicula = "4D"
                                except NoSuchElementException:
                                    pass
                                
                                horas = formato.find_elements(By.CLASS_NAME, 'btnhorario')
                                
                                if not horas:
                                    #sin horas, igual guardamos registro “sin hora”
                                    dataset['Country'].append(determine_country(navegador.current_url))
                                    dataset['Theater'].append(nombre_cine)
                                    dataset['Date'].append(fecha_val)
                                    dataset['Time'].append('')
                                    dataset['Movie'].append(titulo)
                                    dataset['Format'].append(f"{formato_pelicula} {tipo_formato}".strip())
                                else:
                                    for hora in horas:
                                        hora_texto = hora.text
                                        
                                        dataset['Country'].append(determine_country(navegador.current_url))
                                        dataset['Theater'].append(nombre_cine)
                                        dataset['Date'].append(fecha_val)
                                        dataset['Time'].append(hora_texto)
                                        dataset['Movie'].append(titulo)                                    
                                        dataset['Format'].append(formato_pelicula + " " + tipo_formato)

                except NoSuchElementException:
                   # print(f"Algunos elementos no se encontraron en el cine procesado.")
                    continue
    else:
        try:
            contenido_cartelera_principal = esperar_y_encontrar_elemento(By.CLASS_NAME, 'contenido-cartelera-principal')
            if contenido_cartelera_principal:
                list_billboards = contenido_cartelera_principal.find_element(By.ID, 'listBillboards')
                billboards = list_billboards.find_elements(By.CLASS_NAME, 'ScheduleMovie__ScheduleMovieComponent-sc-7752wm-0')

                for billboard in billboards:
                    try:
                        nombre_cine_elemento = billboard.find_element(By.TAG_NAME, 'h2')
                        nombre_cine = nombre_cine_elemento.text if nombre_cine_elemento else "Desconocido"

                        if flag == 1:
                            print(f"Procesando cartelera del cine: {nombre_cine}")

                        encabezado_elemento = billboard.find_element(By.TAG_NAME, 'h3')
                        encabezado = encabezado_elemento.text if encabezado_elemento else "Sin encabezado"

                        peliculas = billboard.find_elements(By.CLASS_NAME, 'SingleScheduleMovie__SingleScheduleComponent-sc-1n3hti2-0')
                        for pelicula in peliculas:
                            titulo = pelicula.find_element(By.CLASS_NAME, 'nombre').find_element(By.TAG_NAME, 'h3').text
                            formatos = pelicula.find_element(By.CLASS_NAME, 'contenedor-formatos').find_elements(By.CLASS_NAME, 'formato')
                            for formato in formatos:
                                tipo_formato = formato.find_element(By.CLASS_NAME, 'formato-nombre').text
                                formato_final = ""
                                horas = formato.find_element(By.CLASS_NAME, 'horas').find_elements(By.TAG_NAME, 'a')
                                
                                if not horas:
                                    try:
                                        imagen_formato = formato.find_element(By.XPATH, ".//img[contains(@src, '3d.png')]")
                                        if imagen_formato:
                                            formato_final = "3D"
                                    except NoSuchElementException:
                                        pass
                                    
                                    try:
                                        imagen_formato = formato.find_element(By.XPATH, ".//img[contains(@src, '4d.png')]")
                                        if imagen_formato:
                                            formato_final = "4D"
                                    except NoSuchElementException:
                                        pass

                                    dataset['Country'].append(determine_country(navegador.current_url))
                                    dataset['Theater'].append(nombre_cine)
                                    dataset['Date'].append(fecha_val)
                                    dataset['Time'].append('')
                                    dataset['Movie'].append(titulo)
                                    dataset['Format'].append(f"{formato_final} {tipo_formato}".strip())

                                else:
                                    for hora in horas:
                                        try:
                                            hora_texto = hora.find_element(By.TAG_NAME, 'p').text.strip()
                                        except Exception:
                                            hora_texto = ""

                                        try:
                                            imagen_formato = formato.find_element(By.XPATH, ".//img[contains(@src, '3d.png')]")
                                            if imagen_formato:
                                                formato_final = "3D"
                                        except NoSuchElementException:
                                            pass
                                        
                                        try:
                                            imagen_formato = formato.find_element(By.XPATH, ".//img[contains(@src, '4d.png')]")
                                            if imagen_formato:
                                                formato_final = "4D"
                                        except NoSuchElementException:
                                            pass

                                        dataset['Country'].append(determine_country(navegador.current_url))
                                        dataset['Theater'].append(nombre_cine)
                                        dataset['Date'].append(fecha_val)
                                        dataset['Time'].append(hora_texto)
                                        dataset['Movie'].append(titulo)                                        
                                        dataset['Format'].append(formato_final + " " + tipo_formato)

                    except NoSuchElementException:
                        #print(f"Algunos elementos no se encontraron en {nombre_cine}.")
                        continue
        except NoSuchElementException:
            print("")


def recorrer_fechas_futuras_panama():
    try:
        select_fechas = esperar_y_encontrar_elemento(By.ID, 'cmbFechas', tiempo_espera=4)
        if not select_fechas:
            return

        sel = Select(select_fechas)
        opciones = select_fechas.find_elements(By.TAG_NAME, 'option')
        if len(opciones) <= 1:
            return

        for idx, opt in enumerate(opciones):
            if idx == 0:
                continue

            valor = (opt.get_attribute('value') or opt.text or "").strip()
            fecha_guardar = a_dd_mm_yyyy_desde_texto_pa(valor if valor else opt.text)
            sel.select_by_value(valor) if valor else opt.click()
            time.sleep(0.5)
            esperar_y_encontrar_elemento(By.CLASS_NAME, 'listaCarteleraHorario', tiempo_espera=6)

            #extraer la cartelera completa
            extraer_horarios_peliculas_pa_gt(dataset=otros_data, fecha_personalizada=fecha_guardar)

    except Exception as e:
        print("")


def recorrer_fechas_futuras_guatemala():
    try:
        sel_elem = esperar_y_encontrar_elemento(By.ID, 'dia', tiempo_espera=4)
        if not sel_elem:
            return

        sel = Select(sel_elem)
        opciones = sel_elem.find_elements(By.TAG_NAME, 'option')
        if len(opciones) <= 1:
            return

        for idx, opt in enumerate(opciones):
            if idx == 0:
                continue

            value = (opt.get_attribute('value') or "").strip()
            if not value:
                continue
            fecha_guardar = a_dd_mm_yyyy_desde_iso(value)
            sel.select_by_value(value)
            time.sleep(0.5)
           
            extraer_horarios_peliculas_pa_gt(dataset=otros_data, fecha_personalizada=fecha_guardar)

    except Exception as e:
        print("")




#Función para procesar una URL
def procesar_url(url):
    global is_panama
    navegador.get(url)
    print(f"Procesando información para {determine_country(url)}")
    
    #Esperar a que el popup aparezca y cerrarlo si está presente
    cerrar_popup()
    is_panama = ".pa" in url

    #procesar los paises el Salvador, honduras y costa rica los cuales usan React
    if any(ext in url for ext in [".sv", ".hn", ".cr"]):
        extraer_cartelera_todos_cines_react()
        return
    

    #procesar los paises guatemala y panamá los cuales NO usan React
    if is_panama:
        esperar_y_encontrar_elemento(By.CLASS_NAME, 'contentBusqueda')
    else:
        esperar_y_encontrar_elemento(By.ID, 'header-principal')

    #para llevar un registro de las ciudades ya procesadas
    ciudades_procesadas = set()

    while True:
        try:
            if is_panama:
                select_ciudad = esperar_y_encontrar_elemento(By.ID, 'cmbCiudades')
            else:
                select_ciudad = esperar_y_encontrar_elemento(By.ID, 'ciudad')

            if not select_ciudad:
                print("No se encontró el selector de ciudad. HTML de la página:")
                print(navegador.page_source[:2000])
                return
            
            ciudades = select_ciudad.find_elements(By.TAG_NAME, 'option')

            if not ciudades:
                print("No se encontraron opciones de ciudad.")
                break

            for ciudad in ciudades[1:]: #ignorando la primera opción
                valor_ciudad = ciudad.get_attribute('value')

                if valor_ciudad and valor_ciudad not in ciudades_procesadas:
                    ciudad.click()
                    ciudades_procesadas.add(valor_ciudad)
                    time.sleep(1.5)

                    if is_panama:
                        esperar_y_hacer_click(By.CSS_SELECTOR, 'input.btn.btnEnviar.btnVerCartelera')
                    else:
                        esperar_y_hacer_click(By.XPATH, '//button[text()="VER CARTELERA"]')
                    time.sleep(1)

                    #extracción de cartelera
                    extraer_horarios_peliculas_pa_gt(flag=1)

                    #FUTURAS FECHAS
                    try:
                        if is_panama:
                            recorrer_fechas_futuras_panama()
                        else:
                            recorrer_fechas_futuras_guatemala()
                    except Exception as e:
                        print(f"Error obteniendo fechas futuras: {e}")

                    if is_panama:
                        select_ciudad = esperar_y_encontrar_elemento(By.ID, 'cmbCiudades')
                    else:
                        select_ciudad = esperar_y_encontrar_elemento(By.ID, 'ciudad')

                    ciudades = select_ciudad.find_elements(By.TAG_NAME, 'option')

        except StaleElementReferenceException:
            #print("Elemento obsoleto encontrado, actualizando la lista de ciudades.")
            continue

        if len(ciudades_procesadas) >= len([ciudad for ciudad in select_ciudad.find_elements(By.TAG_NAME, 'option')[1:] if ciudad.get_attribute('value')]):
            #print("Todas las ciudades han sido procesadas.")
            break




## Flujo inicial de ejecución

urls = [
    "https://cinepolis.com.sv/",
    "https://cinepolis.com.hn/",
    "https://cinepolis.com.cr/",
    "https://cinepolis.com.gt/",
    "https://cinepolis.com.pa/"
]

for url in urls:
    procesar_url(url)

navegador.quit()



#crear el DataFrame y guardar en Excel
df = pd.DataFrame(data)
df2 = pd.DataFrame(otros_data)

#ordenando el dataframe de otros horarios por país, cine, fecha y película
df2['Date'] = pd.to_datetime(df2['Date'], format='%d-%m-%Y', errors='coerce')
df2 = df2.sort_values(by=['Country', 'Theater', 'Date', 'Movie'], ascending=[True, True, True, True])
df2['Date'] = df2['Date'].dt.strftime('%d-%m-%Y')


filename = f'Cinepolis_{datetime.now().strftime("%d-%m-%Y-%H-%M-%S")}.xlsx'
with pd.ExcelWriter(filename) as writer:
    df.to_excel(writer, sheet_name='hoy', index=False)
    df2.to_excel(writer, sheet_name='otros horarios', index=False)

#ajustar el ancho de las columnas para todas las hojas
wb = load_workbook(filename)
#recorrer cada hoja
for ws in wb.worksheets:
    for column in ws.columns:
        max_length = 0
        column_letter = column[0].column_letter
        for cell in column:
            try:
                if cell.value and len(str(cell.value)) > max_length:
                    max_length = len(str(cell.value))
            except:
                pass
        adjusted_width = (max_length + 2)
        ws.column_dimensions[column_letter].width = adjusted_width

wb.save(filename)
print(f'Archivo Excel guardado como {filename}')

