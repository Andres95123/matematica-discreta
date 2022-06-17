import java.lang.AssertionError;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.Set;

/* 
 * Aquesta entrega consisteix en implementar tots els mètodes annotats amb el comentari "// TO DO".
 * Cada tema té el mateix pes, i l'avaluació consistirà en:
 *
 * - Principalment, el correcte funcionament de cada mètode (provant amb diferents entrades). Teniu
 *   alguns exemples al mètode `main`.
 *
 * - La neteja del codi (pensau-ho com faltes d'ortografia). L'estàndar que heu de seguir és la guia
 *   d'estil de Google per Java: https://google.github.io/styleguide/javaguide.html . No és
 *   necessari seguir-la estrictament, però ens basarem en ella per jutjar si qualcuna se'n desvia
 *   molt.
 *
 * Per com està plantejada aquesta entrega, no necessitau (ni podeu) utilitzar cap `import`
 * addicional, ni mètodes de classes que no estiguin ja importades. El que sí podeu fer és definir
 * tots els mètodes addicionals que volgueu (de manera ordenada i dins el tema que pertoqui).
 *
 * Podeu fer aquesta entrega en grups de com a màxim 3 persones, i necessitareu com a minim Java 8.
 * Per entregar, posau a continuació els vostres noms i entregau únicament aquest fitxer.
 * - Nom 1: Ángel Martínez Carvajal
 * - Nom 2: Andrés Borrás Santos
 * - Nom 3: Toni Maqueda Bestard
 *
 * L'entrega es farà a través d'una tasca a l'Aula Digital abans de la data que se us hagui
 * comunicat i vos recomanam que treballeu amb un fork d'aquest repositori per seguir més fàcilment
 * les actualitzacions amb enunciats nous. Si no podeu visualitzar bé algun enunciat, assegurau-vos
 * que el vostre editor de texte estigui configurat amb codificació UTF-8.
 */
class Entrega {
  /*
   * Aquí teniu els exercicis del Tema 1 (Lògica).
   *
   * Els mètodes reben de paràmetre l'univers (representat com un array) i els predicats adients
   * (per exemple, `Predicate<Integer> p`). Per avaluar aquest predicat, si `x` és un element de
   * l'univers, podeu fer-ho com `p.test(x)`, té com resultat un booleà. Els predicats de dues
   * variables són de tipus `BiPredicate<Integer, Integer>` i similarment s'avaluen com
   * `p.test(x, y)`.
   *
   * En cada un d'aquests exercicis us demanam que donat l'univers i els predicats retorneu `true`
   * o `false` segons si la proposició donada és certa (suposau que l'univers és suficientment
   * petit com per utilitzar la força bruta)
   */
  static class Tema1 {
    /*
     * És cert que ∀x,y. P(x,y) -> Q(x) ^ R(y) ?
     */
    static boolean exercici1(
        int[] universe,
        BiPredicate<Integer, Integer> p,
        Predicate<Integer> q,
        Predicate<Integer> r) {
      // Aplicando ley de implicación queda: ∀x,y. !P(x,y) || (Q(x) ^ R(y))
      for (int i=0; i<universe.length; i++) { //recorrido de 'x'
        for (int j=0; j<universe.length; j++) { //para cada 'x' fijada, recorre todas las 'y'
          boolean proposicion = !p.test(universe[i],universe[j]) || (q.test(universe[i]) && r.test(universe[j]));
          if (!proposicion) {
            return false;
          }
        }
      }
      return true; //si se llega aquí, no se ha encontrado un caso donde no se cumpla
    }

    /*
     * És cert que ∃!x. ∀y. Q(y) -> P(x) ?
     */
    static boolean exercici2(int[] universe, Predicate<Integer> p, Predicate<Integer> q) {
      //Aplicando ley de implicación queda ∃!x. ∀y. !Q(y) || P(x)
      for (int j=0; j<universe.length; j++) { //recorrido para todo 'y'
          int contadorNumeroX = 0; //contador de veces que existe una 'x' para cada 'y'
          for (int i=0; i<universe.length; i++) { //recorrido para todo 'x'
            if (!q.test(j) || p.test(i)) { 
              contadorNumeroX++; //si es verdad, existe una 'x' más para esa 'y'
            }
          }
        if (contadorNumeroX!=1) { //si para alguna 'y' no existe exactamente una 'x', falso
            return false;
        }
      }
      //fin de bucle -> no se ha encontrado 'x' que no exista una única vez, en cada iteración de 'y'
      return true;
    }

    /*
     * És cert que ¬(∃x. ∀y. y ⊆ x) ?
     *
     * Observau que els membres de l'univers són arrays, tractau-los com conjunts i podeu suposar
     * que cada un d'ells està ordenat de menor a major.
     */
    static boolean exercici3(int[][] universe) {
      int filas = universe.length;
      int columnas = universe[0].length;
      boolean yPerteneceAXParaTodoY = true; 
      for (int i=0; i<filas; i++) { //fijamos conjunto 'y'
        boolean yPerteneceAX = false;
        for (int j=0; j<columnas; j++) { //recorremos todos los conjuntos 'x' para cada 'y'
          //miramos para el conjunto 'y' fijado si y contiene a alguno de todos los 'x'
           yPerteneceAX = yPerteneceAX || Arrays.asList(universe[i]).containsAll(Arrays.asList(universe[j]));
        }
        if (!yPerteneceAX) yPerteneceAXParaTodoY = false; //si algún 'y' no pertenece a ningún 'x'
      }
      return !yPerteneceAXParaTodoY;
    }

    /*
     * És cert que ∀x. ∃!y. x·y ≡ 1 (mod n) ?
     */
    static boolean exercici4(int[] universe, int n) {
      for (int i=0; i<universe.length; i++) { //recorrido de todos los 'x'
        int numY = 0; //numero de 'y' para cada 'x'
        for (int j=0; j<universe.length; j++) { //recorrido de todas las 'y'
          if ((universe[i]*universe[j])%n == 1) { //si x·y ≡ 1 (mod n)...
            numY++; //se ha encontrado una 'y' para una x concreta
          }
        }
        if (numY != 1) { //fin de recorrido, se mira si existe una única 'y'
          return false; //si no existe una 'y' o no es única para alguna x, false
        }
      }
      return true;
    }

    /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
     */
    static void tests() {
      // Exercici 1
      // ∀x,y. P(x,y) -> Q(x) ^ R(y)

      assertThat(
          exercici1(
              new int[] { 2, 3, 5, 6 },
              (x, y) -> x * y <= 4,
              x -> x <= 3,
              x -> x <= 3
          )
      );

      assertThat(
          !exercici1(
              new int[] { -2, -1, 0, 1, 2, 3 },
              (x, y) -> x * y >= 0,
              x -> x >= 0,
              x -> x >= 0
          )
      );

      // Exercici 2
      // ∃!x. ∀y. Q(y) -> P(x) ?

      assertThat(
          exercici2(
              new int[] { -1, 1, 2, 3, 4 },
              x -> x < 0,
              x -> true
          )
      );

      assertThat(
          !exercici2(
              new int[] { 1, 2, 3, 4, 5, 6 },
              x -> x % 2 == 0, // x és múltiple de 2
              x -> x % 4 == 0  // x és múltiple de 4
          )
      );

      // Exercici 3
      // ¬(∃x. ∀y. y ⊆ x) ?

      assertThat(
          exercici3(new int[][] { {1, 2}, {0, 3}, {1, 2, 3}, {} })
      );

      assertThat(
          !exercici3(new int[][] { {1, 2}, {0, 3}, {1, 2, 3}, {}, {0, 1, 2, 3} })
      );

      // Exercici 4
      // És cert que ∀x. ∃!y. x·y ≡ 1 (mod n) ?

      assertThat(
          exercici4(
              new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 },
              11
          )
      );

      assertThat(
          !exercici4(
              new int[] { 0, 5, 7 },
              13
          )
      );
    }
  }

  /*
   * Aquí teniu els exercicis del Tema 2 (Conjunts).
   *
   * De la mateixa manera que al Tema 1, per senzillesa tractarem els conjunts com arrays (sense
   * elements repetits). Per tant, un conjunt de conjunts d'enters tendrà tipus int[][].
   *
   * Les relacions també les representarem com arrays de dues dimensions, on la segona dimensió
   * només té dos elements. Per exemple
   *   int[][] rel = {{0,0}, {1,1}, {0,1}, {2,2}};
   * i també donarem el conjunt on està definida, per exemple
   *   int[] a = {0,1,2};
   *
   * Les funcions f : A -> B (on A i B son subconjunts dels enters) les representam donant int[] a,
   * int[] b, i un objecte de tipus Function<Integer, Integer> que podeu avaluar com f.apply(x) (on
   * x és un enter d'a i el resultat f.apply(x) és un enter de b).
   */
  static class Tema2 {
    /*
     * És `p` una partició d'`a`?
     *
     * `p` és un array de conjunts, haureu de comprovar que siguin elements d'`a`. Podeu suposar que
     * tant `a` com cada un dels elements de `p` està ordenat de menor a major.
     */
    static boolean exercici1(int[] a, int[][] p) {
      for (int i=0; i<p.length; i++) { //recorrido de conjuntos de 'p'
        if ( !Arrays.asList(a).containsAll(Arrays.asList(p[i])) ) { 
          return false; //si 'a' no contiene algún conjunto de 'p', devuelve falso
        } 
      }
      return true; //llega aquí si 'a' contiene todo conjunto de 'p'
    }

    /*
     * Comprovau si la relació `rel` definida sobre `a` és un ordre parcial i que `x` n'és el mínim.
     *
     * Podeu soposar que `x` pertany a `a` i que `a` està ordenat de menor a major.
     */
    static boolean exercici2(int[] a, int[][] rel, int x) {
      
      //rel es orden parcial? x es el minimo?
      int numElementosReflexivos = 0;
      boolean antisimetrica = true;
      boolean transitiva = true;
      int valoresRelacionadosConX = 0;

      for (int i=0; i<a.length && antisimetrica && transitiva; i++) { //recorrido de pares de la relación
        int valor1 = rel[i][0];
        int valor2 = rel[i][1];
        /////////////////////// PROPIEDAD TRANSITIVA ///////////////////////
        if ( rel[i][0] == rel[i][1] ) { //si un elemento está relacionado consigo mismo, aumenta el contador
          numElementosReflexivos++;
        } else { /////////////////////// PROPIEDAD ANTISIMÉTRICA ///////////////////////
          // antisimetrica: a R b y b R a -> b=a. Si a=b trivial
          //si la relación es de dos valores distintos, se mira la relación en el otro sentido
          //para cada par de la relación se busca la relación en el otro sentido y se mira si son iguales
          for (int j=0; j<a.length; j++) {
            if (rel[j][0] == valor2 && rel[j][1] == valor1) {
              antisimetrica = antisimetrica && (rel[j][0] == rel[j][1]);
            }
          }
        }
        /////////////////////// TRANSITIVA ///////////////////////
        //valor1 está relacionado con valor2, recorrer otra vez
        for (int j=0; j<a.length; j++) { // buscamos valor3 tal que valor2 R valor3
          if (rel[j][0] == valor2) { //rel[j][1] será el valor3 tal que valor2 R valor3
            int valor3 = rel[j][1];
            transitiva = false; //suponemos que no es transitiva y se busca si valor1 R valor3
            for (int k=0; k<a.length; k++) { //fijado valor3 se mira si valor1 R valor3
              if (rel[k][0] == valor1 && rel[k][1] == valor3) { //si valor1 R valor3 es transitiva
                transitiva = true;
              }
            }
          }
        }
        /////////////////////// COMPROBACIÓN MÍNIMO ///////////////////////
        //si para algún c, x R c, x está relacionado con un elemento más
        if (valor1 == x) valoresRelacionadosConX++; 
      }
      //si no todos los elementos están relacionados consigo mismos, no es un orden (no reflexivo)
      if (numElementosReflexivos!=a.getNumeroElementosDistintos) return false;
      if (!antisimetrica) return false; //si una relación no es antisimétrica no es un orden
      if (!transitiva) return false; //si una relación no es transitiva no es un orden
      // De los apuntes: 'x es mínim si és el més petit: ∀a ∈ A : x ≤ a'
      //si x es el mínimo, x está relacionado con todos 
      if (valoresRelacionadosConX != a.getNumeroElementosDistintos) return false;
      return true;
    }

    static int getNumeroElementosDistintos(int[][] matriz) {

        int[] elementosVistos = new int[matriz.length * matriz[0].length];
        int totalElementos = 0;

        if (contieneZero(matriz)) {

            totalElementos++;

        }

        for (int i = 0; i < matriz.length; i++) {

            for (int j = 0; j < matriz[0].length; j++) {

                if (!enArray(matriz[i][j], elementosVistos)) {

                    elementosVistos[totalElementos] = matriz[i][j];
                    totalElementos++;
                }

            }

        }

        return totalElementos;

    }

    static boolean contieneZero(int[][] matriz) {

        for (int[] array : matriz) {
            for (int valor : array) {

                if (valor == 0) {

                    return true;

                }

            }
        }

        return false;

    }

    static boolean enArray(int valor, int[] array) {

        for (int elemento : array) {

            if (elemento == valor) {

                return true;

            }

        }

        return false;

    }

    /*
     * Suposau que `f` és una funció amb domini `dom` i codomini `codom`.  Trobau l'antiimatge de
     * `y` (ordenau el resultat de menor a major, podeu utilitzar `Arrays.sort()`). Podeu suposar
     * que `y` pertany a `codom` i que tant `dom` com `codom` també estàn ordenats de menor a major.
     */
    static int[] exercici3(int[] dom, int[] codom, Function<Integer, Integer> f, int y) {
      //como máximo el conjunto antiimagen tendrá = tamaño que dom (ej: funcion constante)
      int[] antiImagen = new int[dom.length];
      int cardinalAntiImagen = 0;
      for (int i=0; i<dom.length; i++) {
        if (f.apply(dom[i]) == y) { // si f(i) = y, i es antiimagen de y
          antiImagen[cardinalAntiImagen++] = dom[i]; //se supone que en el dominio no hay valores repetidos
        }
        //antiImagen ya tiene todos los valores correspondientes pero puede tener un tamaño mayor a su cardinal
        //(que haya espacios del array sin antiimagen). El siguiente método lo soluciona, truncando la longitud
        antiImagen = Arrays.copyOfRange(antiImagen, 0, cardinalAntiImagen);
        Arrays.sort(antiImagen);
        return antiImagen;
    }

    /*
     * Suposau que `f` és una funció amb domini `dom` i codomini `codom`.  Retornau:
     * - 3 si `f` és bijectiva
     * - 2 si `f` només és exhaustiva
     * - 1 si `f` només és injectiva
     * - 0 en qualsevol altre cas
     *
     * Podeu suposar que `dom` i `codom` estàn ordenats de menor a major. Per comoditat, podeu
     * utilitzar les constants definides a continuació:
     */
    static final int NOTHING_SPECIAL = 0;
    static final int INJECTIVE = 1;
    static final int SURJECTIVE = 2;
    static final int BIJECTIVE = INJECTIVE + SURJECTIVE;

    static int exercici4(int[] dom, int[] codom, Function<Integer, Integer> f) {
      int[] imagenes = new int[codom.length];
      for (int i=0; i<dom.length; i++) { //crea el conjunto imagen de dom
        imagenes[i] = f.apply(dom[i]);
      }
      
       // COMIENZO INYECTIVIDAD
      int valor = INJECTIVE; //asumimos inyectividad y buscamos un contraejemplo
      for (int i=0; i<imagenes.length; i++) { //recorrido de conjunto imagen
        int apariciones = 0; //veces que aparece una imagen en el conjunto imagen
        for (int j=0; j<imagenes.length; j++) {
          //para cada imagen cuenta el número de veces que aparece
          if (imagenes[i] == imagenes[j]) apariciones++;
        }
        //mínimo aparece una vez, así que apariciones >= 1. Si apariciones > 1 no inyectiva
        if (apariciones > 1) valor = NOTHING_SPECIAL;
      }
      
      // COMIENZO EHXHAUSTIVIDAD
      if ( Arrays.asList(imagenes).containsAll(Arrays.asList(codom)) ) {
        //si imagenes contiene todos los valores de codom -> exhaustiva (e imagenes=codom)
        // (no se usa equals porque puede haber imágenes iguales para entradas distintas)
        valor =+ SURJECTIVE;
      }
      
      return valor;
    }

    /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
     */
    static void tests() {
      // Exercici 1
      // `p` és una partició d'`a`?

      assertThat(
          exercici1(
              new int[] { 1, 2, 3, 4, 5 },
              new int[][] { {1, 2}, {3, 5}, {4} }
          )
      );

      assertThat(
          !exercici1(
              new int[] { 1, 2, 3, 4, 5 },
              new int[][] { {1, 2}, {5}, {1, 4} }
          )
      );

      // Exercici 2
      // és `rel` definida sobre `a` d'ordre parcial i `x` n'és el mínim?

      ArrayList<int[]> divisibility = new ArrayList<int[]>();

      for (int i = 1; i < 8; i++) {
        for (int j = 1; j <= i; j++) {
          if (i % j == 0) {
            // i és múltiple de j, és a dir, j|i
            divisibility.add(new int[] { j, i });
          }
        }
      }

      assertThat(
          exercici2(
              new int[] { 1, 2, 3, 4, 5, 6, 7 },
              divisibility.toArray(new int[][] {}),
              1
          )
      );

      assertThat(
          !exercici2(
              new int[] { 1, 2, 3 },
              new int[][] { {1, 1}, {2, 2}, {3, 3}, {1, 2}, {2, 3} },
              1
          )
      );

      assertThat(
          !exercici2(
              new int[] { 1, 2, 3, 4, 5, 6, 7 },
              divisibility.toArray(new int[][] {}),
              2
          )
      );

      // Exercici 3
      // calcular l'antiimatge de `y`

      assertThat(
          Arrays.equals(
              new int[] { 0, 2 },
              exercici3(
                  new int[] { 0, 1, 2, 3 },
                  new int[] { 0, 1 },
                  x -> x % 2, // residu de dividir entre 2
                  0
              )
          )
      );

      assertThat(
          Arrays.equals(
              new int[] { },
              exercici3(
                  new int[] { 0, 1, 2, 3 },
                  new int[] { 0, 1, 2, 3, 4 },
                  x -> x + 1,
                  0
              )
          )
      );

      // Exercici 4
      // classificar la funció en res/injectiva/exhaustiva/bijectiva

      assertThat(
          exercici4(
              new int[] { 0, 1, 2, 3 },
              new int[] { 0, 1, 2, 3 },
              x -> (x + 1) % 4
          )
          == BIJECTIVE
      );

      assertThat(
          exercici4(
              new int[] { 0, 1, 2, 3 },
              new int[] { 0, 1, 2, 3, 4 },
              x -> x + 1
          )
          == INJECTIVE
      );

      assertThat(
          exercici4(
              new int[] { 0, 1, 2, 3 },
              new int[] { 0, 1 },
              x -> x / 2
          )
          == SURJECTIVE
      );

      assertThat(
          exercici4(
              new int[] { 0, 1, 2, 3 },
              new int[] { 0, 1, 2, 3 },
              x -> x <= 1 ? x+1 : x-1
          )
          == NOTHING_SPECIAL
      );
    }
  }

  /*
   * Aquí teniu els exercicis del Tema 3 (Aritmètica).
   *
   */
  static class Tema3 {
    
    private static int[] algoritmoDeEuclides(int a, int b) {
      if (b>a) { //Comprobación de que a>b, si no se cumple, los intercambia
        int tmp = b;
        b=a;
        a=tmp;
      }
      if (b!=0) { //si b es 0 no se aplica el algoritmo para evitar dividir por 0
        // DECLARACIONES
        int MAX_ITERACIONES = 64;
        int[] r = new int[MAX_ITERACIONES];
        int[] q = new int[MAX_ITERACIONES];
        int[] x = new int[MAX_ITERACIONES];
        int[] y = new int[MAX_ITERACIONES];
        r[0]=a;
        r[1]=b;
        x[0]=1;
        x[1]=0;
        y[0]=0;
        y[1]=1;
        q[1]=(a/b);
        int i=1;
        while (r[i]!=0) { //Aplicacion del algoritmo extendido
          q[i] = r[i-1]/r[i];
          r[i+1] = r[i-1]%r[i];
          x[i+1] = x[i-1] - q[i]*x[i];
          y[i+1] = y[i-1] - q[i]*y[i];
          i++;
        }
        int mcd = (x[i-1]*a + y[i-1]*b);
        int[] resultado = {q[i-1], x[i-1], y[i-1], mcd};
        return resultado;
      }
      return null;
    }
    
    /*
     * Donat `a`, `b` retornau el màxim comú divisor entre `a` i `b`.
     *
     * Podeu suposar que `a` i `b` són positius.
     */
    static int exercici1(int a, int b) {
      int[] resAlgoritmo = algoritmoDeEuclides(a,b);
      int mcd = resAlgoritmo[3];
      return mcd;
    }

    /*
     * Es cert que `a``x` + `b``y` = `c` té solució?.
     *
     * Podeu suposar que `a`, `b` i `c` són positius.
     */
    static boolean exercici2(int a, int b, int c) {
      int[] resAlgoritmo = algoritmoDeEuclides(a,b);
      int mcd = resAlgoritmo[3];
      if (c % mcd == 0) {
        return true;
      } else {
        return false;
      }
    }

    /*
     * Quin es l'invers de `a` mòdul `n`?
     *
     * Retornau l'invers sempre entre 1 i `n-1`, en cas que no existeixi retornau -1
     */
    static int exercici3(int a, int n) {
      int valor = a%n;
      if (valor != 0) { //si el valor es 0, no puede ser invertible
        int[] resAlgoritmo = algoritmoDeEuclides(n,a);
        int mcd = resAlgoritmo[3];
        int x = resAlgoritmo[1];
        if (mcd == 1) { //si el mcd es 1, devuelve el resultado del algoritmo
          return x; //revisar que valor se devuelve
        }
      }

      return -1;
    }

    /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
     */
    static void tests() {
      // Exercici 1
      // `mcd(a,b)`

      assertThat(
              exercici1(2, 4) == 2
      );

      assertThat(
              exercici1(1236, 984) == 12
      );

      // Exercici 2
      // `a``x` + `b``y` = `c` té solució?

      assertThat(
              exercici2(4,2,2)
      );
      assertThat(
              !exercici2(6,2,1)
      );
      // Exercici 3
      // invers de `a` mòdul `n`
      assertThat(exercici3(2, 5) == 3);
      assertThat(exercici3(2, 6) == -1);
    }
  }

  static class Tema4 {
    /*
     * Donada una matriu d'adjacencia `A` d'un graf no dirigit, retornau l'ordre i la mida del graf.
     */
    static int[] exercici1(int[][] A) {
      int[] ordreIMida = new int[2];
      ordreIMida[0] = A.length; //orden = num vértices = dimensión de A
      ordreIMida[1] = 0;
      for (int i=0; i<A.length; i++) { //recorrido matriz triangular
        for (int j=0; j<=i; j++) {
          if (i==j) {
            ordreIMida[1] += 2*A[i][j]; //diagonal -> lazo, cuenta doble
          } else {
            ordreIMida[1] += A[i][j];
          }
        }
      }
      return ordreIMida;
    }

    /*
     * Donada una matriu d'adjacencia `A` d'un graf no dirigit, digau si el graf es eulerià.
     */
    static boolean exercici2(int[][] A) { 
      // BÚSQUEDA DE UNA NODO DE GRADO IMPAR RECORRIENDO LA MATRIZ //
      for (int i=0; i<A.length; i++) { //recorrido de filas de la matriz
        int grado = 0;
        for (int j=0; j<A.length; j++) { //se fija el nodo de fila 'i' y se comprueban sus aristas
          if (A[i][j]==1) { //si hay arista, aumenta el grado
            if (i==j) {
              grado += 2; // i=j -> diagonal principal -> hay lazo
            } else {
              grado++; //arista sin lazo, suma 1 al grado
            }
          }
        }
        //fin de recorrido de columnas, se tiene el grado del nodo de la fila i
        if (grado % 2 != 0) return false; //si el grado de un nodo es impar, no euleriano
      }
      return true; //no se han encontrado nodos de grado impar, euleriano
      
    }

    /*
     * Donat `n` el número de fulles d'un arbre arrelat i `d` el nombre de fills dels nodes interiors i de l'arrel,
     * retornau el nombre total de vèrtexos de l'arbre
     *
     */
    static int exercici3(int n, int d) {
      
      // el número de hijos de la raíz debe ser también d, si no, el número de vértices no es único...
      
        int nodosInteriores = (d-n)/(1-d);
        
        int vertices = n + nodosInteriores + 1;

        return (int) vertices; // TO DO

    }

    /*
     * Donada una matriu d'adjacencia `A` d'un graf connex no dirigit, digau si el graf conté algún cicle.
     */
    
    /* Para resolver este ejercicio se ha usado la siguiente proposición:
    Proposició: Sigui a^(k)_i,j l’entrada (i, j) de la matriu A^k.
    Aleshores a^(k)_i,j és igual al nombre de recorreguts vi vj de longitud k

    Entonces
    */
    static boolean exercici4(int[][] A) { //AVISO está mal, no se hace así.....
      int numNodos = A.length;

     int contadorAristas = 0;
     for (int i=0; i<A.length; i++) {
      for (int j=0; j<A[i].length; j++) {
        if (A[i][j] == 1) {
          if (i == j) {
          contadorAristas = contadorAristas + 2; //lazo, cuenta como si fuesen dos
          // Al dividir entre 2 por el lema de las encajadas de manos contará como una arista
          } else {
            contadorAristas++;
          }
        }
        
      }
     }
     contadorAristas /= 2;
     return (contadorAristas > numNodos-1);
    }
    
    /*
    Producto de matrices para ver si el número de recorridos a un nodo es único
    Suponemos que el tamaño es adecuado porque la matriz de adyacencia es cuadrada
    */
    static int[][] productoMatricesCuadradas(int[][] A, int[][] B) {
      int[][] res = new int[A.length][A[0].length];
      for (int i = 0; i < A.length; i++) { //recorrido filas
        for (int j = 0; j < B[0].length; j++) { //recorrido columnas
          for (int k = 0; k < A[0].length; k++) {
            // aquí se multiplica la matriz
            res[i][j] += A[i][k] * B[k][j];
          }
        }
      }
      return res;
    }
    
    /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
     */
    static void tests() {
      // Exercici 1
      // `ordre i mida`

      assertThat(
              Arrays.equals(exercici1(new int[][] { {0, 1, 0}, {1, 0, 1}, {0,1, 0}}), new int[] {3, 2})
      );

      assertThat(
              Arrays.equals(exercici1(new int[][] { {0, 1, 0, 1}, {1, 0, 1, 1}, {0 , 1, 0, 1}, {1, 1, 1, 0}}), new int[] {4, 5})
      );

      // Exercici 2
      // `Es eulerià?`

      assertThat(
              exercici2(new int[][] { {0, 1, 1}, {1, 0, 1}, {1, 1, 0}})
      );
      assertThat(
              !exercici2(new int[][] { {0, 1, 0}, {1, 0, 1}, {0,1, 0}})
      );
      // Exercici 3
      // `Quants de nodes té l'arbre?`
      assertThat(exercici3(5, 2) == 9);
      assertThat(exercici3(7, 3) == 10);

      // Exercici 4
      // `Conté algún cicle?`
      assertThat(
              exercici4(new int[][] { {0, 1, 1}, {1, 0, 1}, {1, 1, 0}})
      );
      assertThat(
              !exercici4(new int[][] { {0, 1, 0}, {1, 0, 1}, {0, 1, 0}})
      );

    }
  }


  /*
   * Aquest mètode `main` conté alguns exemples de paràmetres i dels resultats que haurien de donar
   * els exercicis. Podeu utilitzar-los de guia i també en podeu afegir d'altres (no els tendrem en
   * compte, però és molt recomanable).
   *
   * Podeu aprofitar el mètode `assertThat` per comprovar fàcilment que un valor sigui `true`.
   */
  public static void main(String[] args) {
    Tema1.tests();
    Tema2.tests();
    Tema3.tests();
    Tema4.tests();
  }

  static void assertThat(boolean b) {
    if (!b)
      throw new AssertionError();
  }
}

// vim: set textwidth=100 shiftwidth=2 expandtab :

//si
