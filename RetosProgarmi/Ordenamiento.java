import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;
public class Ordenamiento {
    public static void main(String[] args) {
        int[] numeros = {5, 2, 8, 1, 9};
        Arrays.sort(numeros);
        

        System.out.println(Arrays.toString(numeros));


        TreeSet<Integer> numerosOrdenados = new TreeSet<>();
        numerosOrdenados.add(5);
        numerosOrdenados.add(2);
        numerosOrdenados.add(8);
        numerosOrdenados.add(1);
        numerosOrdenados.add(9);

        System.out.println(numerosOrdenados);


        TreeMap<String, Integer> mapaOrdenado = new TreeMap<>();

        mapaOrdenado.put("Cinco", 5);
        mapaOrdenado.put("Dos", 2); 
        mapaOrdenado.put("Ocho", 8);
        mapaOrdenado.put("Uno", 1);
        mapaOrdenado.put("Nueve", 9);
        System.out.println(mapaOrdenado);
    }
}
