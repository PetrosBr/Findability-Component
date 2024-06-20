# Findability Component
This is the first version of the Findability Component within Data Governance Platform of MobiSpaces project. Its primary goal is to allow users to create Metadata for their datasets and store them into a Virtuoso database. The metadata are stored in RDF format so that they can be discovered using simple SPARQL queries in the virtuoso SPARQL endpoint. The second version will enhance Findability of MobiSpaces datasets by using a more user-friendly interface (via API) without the use of SPARQL.
## Setup

```
findabilityComponent
├─┬ backend     → backend module with Java code for database interactions
│ ├── src
│ └── pom.xml
├─┬ frontend    → frontend module with Vue.js code
│ ├── src
│ └── pom.xml
└── pom.xml     → Maven parent pom managing both modules
```

## Application configuration
After intsalling virtuoso, predefined codelists must be uploaded to virtuoso to support Metadata creation and storage.
Configure virtuoso at the files (currently Virtuoso uses a localhost endpoint):

```
src/main/resources/application.properties
src/test/resources/application.properties
```

Configuration parameters:

```
virtuoso.endpoint=localhost
virtuoso.port=1111
virtuoso.user=dba
virtuoso.password=dba
virtuoso.frequencyCodelistGraph=http://localhost:8890/codelists
virtuoso.languageCodelistGraph=http://localhost:8890/codelists
virtuoso.countryCodelistGraph=http://localhost:8890/codelists
virtuoso.dataGraph=http://testgraph.org
```

| Parameter              | Description |
| ---- | ----|
| virtuoso.endpoint      | The virtuoso endpoint URL |
| virtuoso.port          | The port virtuoso listens | 
| virtuoso.user          | Username to connect to virtuoso |
| virtuoso.password      | Password to connect to virtuoso |
| virtuoso.frequencyCodelistGraph | The GRAPH where the frequency code list is stored (all codelists can be stored at the same GRAPH)|
| virtuoso.languageCodelistGraph | The GRAPH where the language code list is stored  (all codelists can be stored at the same GRAPH)|
| virtuoso.countryCodelistGraph | The GRAPH where the country code list is stored  (all codelists can be stored at the same GRAPH)|
| virtuoso.dataGraph     | The GRAPH to store the dataset metadata |

## To run the application 

Inside the root directory, run a: 

```
mvn validate
```

Clean & install the application: 

```
mvn clean install
```

Run the application:

```
mvn --projects backend spring-boot:run
```

The application interface (frontend) by default is available at http://localhost:8098/sae/.

## Build and run with Docker

Run Docker build:

```
docker build . --tag findability-component:latest
```

Start the Docker container:

```
docker run -d -p 8098:8098 --name myfcomponent findability-component
```