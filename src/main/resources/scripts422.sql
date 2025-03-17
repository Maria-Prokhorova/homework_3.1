create table cars (
 id serial primary key,
 brand text,
 model text,
 cost integer check (cost>0)
 )

 insert into cars (id, brand, model, cost) values (1, 'Audu', 'TT', 5000000);
 insert into cars (id, brand, model, cost) values (2, 'BMW', 'x7', 3000000);
 insert into cars (id, brand, model, cost) values (3, 'Lada', 'Kaline', 450000);
 insert into cars (id, brand, model, cost) values (4, 'Sybara', 'sport', 1500000);
 insert into cars (id, brand, model, cost) values (5, 'lexus', '5', 7000000);


 create table people (
 id serial primary key,
 name text not null,
 age integer not null,
 license BOOLEAN,
 id_car integer references cars (id)
 )

 insert into people (id, name, age, license) values (1, 'Ivan', 17, 'no');
 insert into people (id, name, age, license, id_car) values (2, 'Maria', 25, 'yes', 1);
 insert into people (id, name, age, license) values (3, 'Alex', 35, 'no');
 insert into people (id, name, age, license, id_car) values (4, 'Vova', 45, 'yes', 3);
 insert into people (id, name, age, license, id_car) values (5, 'Vova', 45, 'yes', 2);

 select people.name, people.age, people.license, cars.brand, cars.model
 from people left join cars on people.id_car=cars.id;

