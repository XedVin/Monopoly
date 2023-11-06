# Cambios
Alquiler caer -> aquiler solar(con todas las modificaciones) + alquileres edificios

# Edificios
- Valor
- Alquiler

El alquiler de la casilla será la suma de los alquileres de todos los edificios.

## Número de edificios máximos
Si el grupo tiene sólo dos casillas, en todo el grupo se pueden construir:
- 2 hoteles
- 2 casas
- 2 piscinas
- 2 pistas de deporte

Si el grupo tiene tres casillas, en todo el grupo se pueden construir:
- 3 hoteles
- 3 casas
- 3 piscinas
- 3 pistas de deporte
## Tipos de edificios
#### Casas
Valor: 60% del valor inicial del solar.
Alquiler:
- Primera casa: 5 veces el alquiler del solar
- Segunda casa: 15 veces el alquiler del solar
- Tercera casa: 35 veces el alquiler del solar
- Cuarta casa: 50 veces el alquiler del solar
Condiciones para poder construir: 
- Estar en la casilla.
- Haber caído más de dos veces en esa misma casilla **o** poseer todo el grupo de casillas de ese color.
#### Hotel
Valor: 60% del valor inicial del solar.
Alquiler: 70 veces el alquiler del solar.
Condiciones para poder construir:
- Haber construido cuatro casas en ese solar(Se deben substituir las casas por el hotel).
#### Piscina
Valor: 40% del valor inicial del solar.
Alquiler: 25 veces el alquiler del solar.
Condiciones para poder construir:
- Haber construido al menos un hotel y dos casas.
#### Pista de Deporte
Valor: 125% del valor inicial del solar.
Alquiler: 25 veces el alquiler del solar.
Consdiciones para poder construir:
- Haber construido al menos dos hoteles.
## Ideas
Clase separada.
Id: tipo-numero.(?)
Almacenar los edicios en una Map por tipos en cada una de las casillas.
Para listar los edificios por grupo, se obtienen las casillas de ese grupo, y se imprimen cada uno de los edificios de esa casilla.
Para listar los edificios construidos se itera por cada jugador,por cada una de sus casillas y por cada un de sus edificios y los imprime.(Mal)
Alternativa: Que el objeto *Monopoly* tenga todos los edificios creados.
Alternativa: Que se itere por todos los solares y por todos los edificios.(Un poco mejor)
### Edificar
Formato: edificar <tipo>
Precondición: Cumple las condiciones para poder construir del tipo en concreto, puede pagar el valor, es de su propiedad y ¿que no haya otro edificio en otra casilla de ese mismo grupo?
### Vender
Formato: vender <tipo> <nombre> <cantidad>.
Precondición: Que la propiedad le pertenezca al jugador y que la cantidad sea la correcta.

---

# Hipotecas
Valor:
- Casillas de solar, transporte o servicios: mitad de su valor inicial.
- Edificio: mitad de su valor inicial.
## Ideas
Que sea una lista de casillas en cada jugador, y que cada casilla tenga un atributo para si es hipotecado y otro para el valor.
### Hipotecar
Formato: hipotecar <nombre>
Precondición: La casilla no está hipotecada y le pertenece al jugador que quiere hipotecarla. Si la casilla es de solar y tiene edificios, se deberán vender antes de hipotecar.
Al hipotecar una propiedad, el jugador recibe el valor de la hipoteca por parte de la **banca**. Si otro jugador cae en la casilla hipotecada, no se deberá hacer ningún pago.
### Deshipotecar
Formato: deshipotecar <nombre>
Precondición: La casilla está hipotecada y le pertenece al jugador que quiere hipotecarla.
Costo:
- Casillas de solar, transporte o servicios: Misma cantidad que recibió más un recargo del 10%.

---

# Cartas
- Tipo
- Descripción
## Ideas
Clase separada.
Que sea un Map de Arrays que se accede con el tipo de casilla en la clase Monopoly.(No muy bien)
Que sean dos arrays en la clase Monopoly.(Tampoco muy bien)
### Barajar
Se debe ejecutar cada vez que se cae en la casilla de Suerte o Caja de Comunidad.
Se barajan las cartas, asignandoles una posición aleatoria en el mazo.
### Elegir carta
Se le pregunta al jugador que carta quiere y este debe devolver un número del 1 al 10.
Se mostrará la carta con su descripción.
### Realizar accion
Se realiza la acción de la carta y se le comunica al jugador el resultado. Si estas involucran un pago, primero se intentará hacerlo de la fortuna del jugador, pero en el caso
de que no sea suficiente, se le indicará al jugador que hipoteque una propiedad. Si la acción implica desplazarse a una casilla, se aplicaran las mismas decisiones que si llegara
de forma normal.
