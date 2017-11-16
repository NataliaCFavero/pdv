# Project - Pdv
Project to save and find near pdvs.

## Prerequisites
* Java SE 8
* Gradle 2.1

## Getting Started
	docker pull cassandra
	docker run --name pdv-cassandra -d cassandra:3.0.0

## Building Project
	gradle clean build

## Run Application
	gradle bootRun

## Usage
 - GET: http://localhost:8080/pdv/{id}
 	Return one Pdv
 - POST: http://localhost:8080/pdv
 	Save one Pdv
 	{
		"tradingName": "Adega da Cerveja - Pinheiros",
		"ownerName": "Zé da Silva",
		"document": "27.836.966/0001-10", 
		"coverageArea": { 
		  "type": "MultiPolygon", 
		  "coordinates": [
		    [[[30.0, 20.0], [45.0, 40.0], [10.0, 40.0], [30.0, 20.0]]], 
		    [[[15.0, 5.0], [40.0, 10.0], [10.0, 20.0], [5.0, 10.0], [15.0, 5.0]]]
		  ]
		}, 
		"address": { 
		  "type": "Point",
		  "coordinates": [-46.57421, -21.785741]
		}
	}
 - GET: http://localhost:8080/pdv/{lat}/{lng}
 	Return the Pdv near
 	 {
        "id": 1, 
        "tradingName": "Adega da Cerveja - Pinheiros",
        "ownerName": "Zé da Silva",
        "document": "1432132123891/0001", //CNPJ
        "coverageArea": { 
          "type": "MultiPolygon", 
          "coordinates": [
            [[[30, 20], [45, 40], [10, 40], [30, 20]]], 
            [[[15, 5], [40, 10], [10, 20], [5, 10], [15, 5]]]
          ]
        }
        "address": { 
          "type": "Point",
          "coordinates": [-46.57421, -21.785741]
        }
    }

## Deployment
	