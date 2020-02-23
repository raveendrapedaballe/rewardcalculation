DROP TABLE IF EXISTS transaction;
 
CREATE TABLE transaction (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  user_id VARCHAR(250) NOT NULL,
  month VARCHAR(250) NOT NULL,
  transaction_amount VARCHAR(250) NOT NULL
);


insert into transaction
values(1,'abc',1,200);

insert into transaction
values(2,'abc',1,80);

insert into transaction
values(3,'abc',1,40);

insert into transaction
values(4,'abc',1,100);

insert into transaction
values(5,'abc',2,200);

insert into transaction
values(6,'abc',2,80);

insert into transaction
values(7,'abc',2,40);

insert into transaction
values(8,'abc',2,100);

insert into transaction
values(9,'abc',3,200);

insert into transaction
values(10,'abc',3,80);

insert into transaction
values(11,'abc',3,40);

insert into transaction
values(12,'abc',3,100);