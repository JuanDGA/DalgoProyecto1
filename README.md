# Proyecto 1
**Realizado por:**
- Juan David Guevara Arévalo - 202116875
- Jose David Martínez Oliveros - 202116677
## 1. Algoritmo de solución
Para solucionar el problema, se decidió abordarlo en 3 etapas:
1. Determinar un algoritmo para ordenar las torres (sin pensar en hacerlo de la mejor forma)
2. Mejorar el algoritmo para obtener la menor cantidad de pasos.
3. Optimizar el algoritmo para reducir el tiempo de ejecución.
### 1.1. Determinar un algoritmo para solucionar el problema
Para llegar a un algoritmo, se planteo la situación más pequeña posible (donde tan solo hay 2 torres) y planteamos una ecuación de recurrencia que solucionaría dicha situación.
La ecuación encontrada es la siguiente:
```
dp[i] = dp[i] + ceil((dp[i + 1] - dp[i]) / 2) if dp[i + 1] > dp[i]
dp[i + 1] = dp[i + 1] - ceil((dp[i + 1] - dp[i]) / 2) if dp[i + 1] > dp[i]
```
La anterior ecuación, aumenta la torre $i$ la cantidad de pasos requeridos, a la vez que decrementa la torre $i+1$.
Sin embargo, para completar el algoritmo, es necesario iterar *"Mover $i$"* correctamente para conseguir un algoritmo funcional.
Para ello, identificamos el caso en el que hay 3 torres, y se está revisando la torre del medio. Una vez aplicada la ecuación, es necesario revisar que la torre del medio siga siendo más alta o igual que la anterior, si no lo es, se decrementa $i$, de lo contrario se incrementa.
Con dicha estrategia, el algoritmo ya ordena las torres correctamente.
### 1.2. Obtener la menor cantidad de pasos
Dado que a una torre es posible ponerle fichas de cualquier torre adyacente, es necesario actualizar el algoritmo para que tome las fichas del mejor lado y no solo de la izquierda. Para ello, es necesario calcular la diferencia de altitud entre las torre $i$ e $i + 1$, y ver si podemos obtener dicha cantidad de la torre izquierda sin hacerla más pequeña que la altura objetivo. Esto asegura que, de ser posible, nivelaremos la torre actual asegurando que siga siendo menor o igual a la de la izquierda.
La ecuación de recurrencia final sería la siguiente:
```
diff = dp[i + 1] - dp[i] # Por simplicidad

dp[i] = dp[i] if i == n - 1 or diff <= 0
dp[i] = dp[i] + ceil(diff / 2) if i == 0 or floor((dp[i-1] - dp[i]) / 2) < diff # Desde i + 1
dp[i] = dp[i] + min(floor((dp[i-1] - dp[i]) / 2), diff) # Desde i - 1
```
*Por simplicidad se omitió la actualización de $dp[i - 1]$ y $dp[i + 1]$ respectivamente.*

Implementando la anterior ecuación, las torres no solo terminarán en el orden correcto, sino que el proceso se realizará en la menor cantidad de pasos posibles.
### 1.3. Optimización
*A partir de aquí la implementación en código se aleja un poco de la ecuación de recurrencia definida, sin embargo, sigue la misma estrategia de tomar de la izquierda si es posible, de lo contrario de la derecha*

Para optimizar el algoritmo fue necesario reducir considerablemente la cantidad de iteraciones realizada. Para ello se enfrentaron 2 problemas:
- Agilizar la cantidad de iteraciones requeridas para ordenar casos sencillos.
- Identificar desde que punto las torres tienen el mismo tamaño

El primer punto fue sencillo, ya que es fácil saber como deben quedar las torres si hay $N$ torres seguidas del mismo tamaño $n$ y luego aparece una torre de altura $n + k$.

Tras varias pruebas, la formula identificada para calcular la cantidad de pasos necesarios para ordenar el caso descrito fue la siguiente:

$B = \frac{k}{N}$
$r = k - B * N$
$R = N - r$
$steps = \frac{1}{2}N(B + 1)(N+1) - \frac{1}{2}R(R+1)$

Con la disponibilidad de esta formula, lo único que resta es hacer traqueo correcto de la posición desde la que se puede aplicar la formula para identificar $N$ de forma adecuada.
Para ello, se creó una variable auxiliar con el objetivo de guardar la posición desde la cual hay torres del mismo tamaño seguidas. La forma de aumentar el valor de dicha variable es sencilla, pues si en el recorrido actual de las torres la siguiente es más baja, se aumenta el valor de dicha variable, de lo contrario no se modifica.
Cabe resaltar que una vez se actualizan las torres, es necesario re-calcular el valor de la variable, lo cuál es sencillo en el caso $N > k$, de lo contrario, simplemente $i$ se actualiza con valor 0.
*Para mejorar aún más la complejidad temporal en lugar de utilizar una variable, se puede utilizar una cola en la que se van guardando las posiciones, pero se empieza a sacrificar memoria y la diferencia no es tan significativa.*
## 2. Análisis de complejidades espacial y temporal
Mejor caso: $O(N)$ *Todo está ordenado, o solo hay una torre mal*
Peor caso: $O(N²)$

El ciclo principal del algoritmo tiene potencial de ejecutarse n² veces, pues hay que recordar que en ocasiones $i$ se decrementa o se situa directamente en 0