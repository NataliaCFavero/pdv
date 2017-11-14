CREATE KEYSPACE IF NOT EXISTS pdv
  WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };
  
CREATE TABLE pdv.pdv(
	id UUID,
 	tradingName text,
 	ownerName text,
 	document text,
 	coverageArea list<frozen <list<frozen <list<list <double>>>>>>,
 	address list<double>,
 	PRIMARY KEY (id)
);
CREATE INDEX ON pdv.pdv (document);

INSERT INTO pdv.pdv(id, tradingName, ownerName, document, coverageArea, address)
 values(d82e8b25-92a7-43c5-9bea-29b672bedbe3, 'Cervejaria da Cidade', 'José José', 
 '08485971000180', 
 [[[[30,20],[45,40],[10,40],[30,20]]], [[[30,20],[45,40],[10,40],[30,20]]]],
 [40.0, 30.0]
);

INSERT INTO pdv.pdv(id, tradingName, ownerName, document, coverageArea, address)
 values(8f915b11-9fba-4926-b4d7-1f0581a59156, 'Casa da Cerveja', 'José Maria', 
 '43705344000109', 
 [[[[30,20],[45,40],[10,40],[30,20]]], [[[30,20],[45,40],[10,40],[30,20]]]],
 [40.0, 30.0]
);

INSERT INTO pdv.pdv(id, tradingName, ownerName, document, coverageArea, address)
 values(53d9f5b3-f08b-4118-8434-107dddb6e69e, 'Cervejaria da Skina', 'José Maria José', 
 '11607462000198', 
 [[[[30,20],[45,40],[10,40],[30,20]]], [[[30,20],[45,40],[10,40],[30,20]]]],
 [40.0, 30.0]
);