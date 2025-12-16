# Proyecto G9: ElectricSim – Pipeline de datos y monitoreo en tiempo real

## Descripción del proyecto

**ElectricSim** es una plataforma **orientada al monitoreo y análisis en tiempo real** del **consumo eléctrico** en el **Gran Buenos Aires (GBA)**.
Este repositorio corresponde al **módulo de adquisición, procesamiento y distribución de datos**, encargado de integrar múltiples fuentes externas, normalizar la información y exponerla tanto para visualización como para consumo por modelos de machine learning.

El sistema está diseñado como un pipeline de datos continuo, desacoplado y escalable, utilizando Apache Kafka como backbone de mensajería para los flujos en tiempo real, y PostgreSQL para la persistencia de información histórica.

<details>
  <summary><i>Características del proyecto</i></summary>
  <ol>
    <li><b>Integración de múltiples fuentes de datos</b></li>
    <p>El sistema unifica información energética oficial, datos meteorológicos y variables astronómicas relevantes para el modelado del consumo eléctrico.</p>

  <li><b>Procesamiento de datos en tiempo real</b></li>
  <p>Los datos actuales son distribuidos mediante Apache Kafka, permitiendo desacoplar la recolección de datos del procesamiento analítico.</p>

  <li><b>Modelo predictivo basado en deep learning</b></li>
  <p>Una red neuronal feedforward permite estimar la demanda eléctrica a partir de variables climáticas, temporales y energéticas.</p>

  <li><b>Inferencia bajo demanda</b></li>
  <p>El sistema expone una API REST que permite realizar predicciones puntuales y simulaciones con datos de pronóstico.</p>

  <li><b>Detección de anomalías en tiempo real</b></li>
  <p>Se detectan desvíos significativos entre valores reales y predichos mediante Server-Sent Events (SSE).</p>
  </ol>
</details>

<details>
  <summary><i>Beneficios</i></summary>
  <ol>
    <li>Anticipación de picos de consumo</li>
    <p>Permite prever aumentos o caídas en la demanda eléctrica con antelación.</p>

  <li>Análisis energético integral</li>
  <p>Combina variables ambientales, temporales y operativas en un único modelo.</p>

  <li>Arquitectura escalable</li>
  <p>El uso de Kafka permite escalar productores y consumidores de datos de forma independiente.</p>

  <li>Aplicación educativa</li>
  <p>Sirve como base para el estudio de sistemas distribuidos y machine learning aplicado.</p>
  </ol>
</details>

<details>
  <summary><i>Tecnologías utilizadas</i></summary>
  <ol>

  <li>Fuentes de datos</li>
  <ul>
    <li>CAMMESA API: datos oficiales de generación y demanda eléctrica</li>
    <li>OpenWeather API: variables meteorológicas</li>
    <li>Sunrise-Sunset API: información astronómica</li>
  </ul>

  <li>Streaming y mensajería</li>
  <ul>
    <li>Apache Kafka</li>
    <li>Kafka Streams</li>
  </ul>

  <li>Backend y procesamiento</li>
  <ul>
    <li>Spring Boot: recolección y exposición de datos</li>
    <li>Python + FastAPI: inferencia, detección de anomalías y SSE</li>
    <li>APIs HTTP/REST</li>
    <li>Server-Sent Events (SSE)</li>
  </ul>

  <li>Machine Learning</li>
  <ul>
    <li>TensorFlow / Keras</li>
    <li>Scikit-learn</li>
    <li>Pandas y NumPy</li>
  </ul>

  <li>Infraestructura</li>
  <ul>
    <li>Docker</li>
    <li>Docker Compose</li>
  </ul>

  </ol>
</details>

---

<h1 id="architecture">Arquitectura del sistema</h1>

<p>ElectricSim se organiza en dos bloques funcionales principales:</p>

<img width="100%" alt="esquema_completo" src="https://github.com/user-attachments/assets/2118839a-ce69-4528-8362-0c713d75b9a6" />

<ul>
  <li><b>Recolección y distribución de datos:</b> las fuentes energéticas, meteorológicas y astronómicas son consultadas de manera periódica por un backend desarrollado en Spring Boot. En este proceso, solo los datos en tiempo real son enviados hacia Apache Kafka, que actúa como sistema de mensajería y desacople entre productores y consumidores. Por otro lado, los datos históricos no pasan por Kafka: quedan disponibles directamente a través de un endpoint específico del backend, desde donde luego son consumidos para el proceso de entrenamiento del modelo. Paralelamente, los datos en tiempo real almacenados en Kafka se utilizan para la visualización continua mediante Grafana. </li>
  <li><b>Entrenamiento del modelo:</b> los datos provenientes de Kafka (en tiempo real) y los datos históricos previamente almacenados son procesados mediante un módulo de preprocesamiento que limpia, transforma y organiza la información. Luego, estos datos alimentan un modelo de deep learning diseñado para predecir la demanda eléctrica. Una vez entrenado, el modelo se expone mediante una API que no solo permite realizar inferencias, sino que también ofrece un endpoint para la obtención de anomalías cada 5 minutos mediante SSE. Además, este bloque integra datos en tiempo real para reentrenar periódicamente el modelo con información más reciente, permitiendo que la red se adapte a los cambios actuales del sistema.</li>



</ul>

---

<h1 id="structure">Estructura del proyecto</h1>

<p>
Este repositorio corresponde al <b>módulo de adquisición, procesamiento y persistencia de datos de ElectricSim</b>. La estructura del proyecto está organizada siguiendo una arquitectura modular, separando claramente las responsabilidades de cada componente del pipeline de datos.
</p>

<pre>
ElectricSim-Data/
│
├── src/
│   └── main/
│       ├── java/dev/str/electricsim/
│       │   ├── cache/          # Cache de datos auxiliares (feriados, calendarios, etc.)
│       │   ├── client/         # Clientes HTTP para consumo de APIs externas
│       │   ├── config/         # Configuración general (Kafka, Streams, Beans)
│       │   ├── controllers/    # Endpoints REST para exposición de datos históricos
│       │   ├── dto/            # Objetos de transferencia de datos
│       │   ├── entity/         # Entidades persistidas en PostgreSQL
│       │   ├── model/          # Modelos de dominio internos
│       │   ├── producers/      # Publicadores de datos hacia Apache Kafka
│       │   ├── repository/     # Acceso a datos y persistencia (JPA / PostgreSQL)
│       │   ├── services/       # Lógica de negocio y orquestación
│       │   ├── streams/        # Procesamiento y enriquecimiento de datos con Kafka Streams
│       │   └── Application.java# Punto de entrada de la aplicación
│       │
│       └── resources/          # Configuración y recursos de la aplicación
│
├── grafana/                   # Dashboards y configuración de visualización
├── docker-compose.yml         # Orquestación de servicios
├── Dockerfile                 # Imagen del servicio de datos
├── pom.xml                    # Dependencias del proyecto (Maven)
├── README.md                  # Documentación del módulo
├── init.sh                    # Script para levantar el proyecto
├── down.sh                    # Script para bajar el proyecto
└── .env                       # Variables de entorno

</pre>

---

<h1 id="scope">Alcance del repositorio</h1>

<p>
Este repositorio contiene <b>únicamente la implementación del pipeline de datos</b> de ElectricSim:
</p>

<ul>
<li>Recolección de datos desde APIs externas</li>

<li>Publicación de eventos en Apache Kafka</li>

<li>Procesamiento y enriquecimiento con Kafka Streams</li>

<li>Persistencia de datos históricos en PostgreSQL</li>

<li>Exposición de datos mediante API REST</li>
</ul>

El modelo de deep learning, la inferencia y la detección de anomalías se desarrollan en repositorios independientes.

---

<h1 id="flow">Flujo de funcionamiento del sistema</h1>

<details>
  <summary><i>Flujo general de ElectricSim</i></summary>
  <ol>
    <li><b>Recolección de datos</b></li>
    <p>El backend consulta periódicamente las APIs de CAMMESA, OpenWeather y Sunrise-Sunset.</p>

  <li><b>Publicación en Kafka</b></li>
  <p>Los datos en tiempo real se publican en tópicos de Apache Kafka.</p>

  <li><b>Consumo y preprocesamiento</b></li>
  <p>El backend analítico consume los datos, los limpia y los transforma.</p>

  <li><b>Inferencia del modelo</b></li>
  <p>El modelo de deep learning predice la demanda eléctrica.</p>

  <li><b>Detección de anomalías</b></li>
  <p>Se comparan valores reales y predichos y se notifican desvíos mediante SSE.</p>

  <li><b>Reentrenamiento periódico</b></li>
  <p>El modelo se reentrena automáticamente con datos recientes.</p>
  </ol>
</details>

---

<h1 id="startup">Puesta en marcha del sistema</h1>

<p>
ElectricSim-Data utiliza <b>Docker</b> y <b>Docker Compose</b> para simplificar la ejecución del entorno completo de desarrollo. Se crearon scripts que facilitan levantar los servicios.
</p>

<p>Para construir las imágenes y levantar los servicios:</p>

```bash
./init.sh
```

<p>Para detener el entorno y liberar los recursos:</p>

```bash
./down.sh
```

<h1 id="authors">Autores</h1>

<ul>
  <li>
    <a href="https://www.linkedin.com/in/agust%C3%ADn-murray-235ab6188/">
      <img align="right" src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" />
    </a>
    <a href="https://github.com/agumurray">
      <img align="right" src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" style="margin-right: 5px;" />
    </a>
    <strong>Murray, Agustín</strong>
    <br clear="right"/>
  </li>

  <li>
    <a href="https://www.linkedin.com/in/ivanpolanis/">
      <img align="right" src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" />
    </a>
    <a href="https://github.com/ivanpolanis">
      <img align="right" src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" style="margin-right: 5px;" />
    </a>
    <strong>Polanis, Iván Valentín</strong>
    <br clear="right"/>
  </li>

  <li>
    <a href="https://www.linkedin.com/in/mateo-romero-5a94b525a/">
      <img align="right" src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" />
    </a>
    <a href="https://github.com/ricrubrom">
      <img align="right" src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" style="margin-right: 5px;" />
    </a>
    <strong>Romero, Mateo</strong>
    <br clear="right"/>
  </li>
</ul>
