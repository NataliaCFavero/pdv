CREATE KEYSPACE IF NOT EXISTS pdv
  WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };
  
CREATE TABLE IF NOT EXISTS pdv.pdv(
	id UUID,
 	tradingName text,
 	ownerName text,
 	document text,
 	coverageArea list<frozen <list<frozen <list<list <double>>>>>>,
 	address list<double>,
 	PRIMARY KEY (id)
);
CREATE INDEX ON pdv.pdv (document);