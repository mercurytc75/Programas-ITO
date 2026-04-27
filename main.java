import java.io.BufferedReader;
import java.io.FileReader;

public class main{
    public static void main(String[] args){
        String Sistemas = "sistemas.txt";
		String Informatica = "informatica.txt";
		String mesclado = "mesclado.txt";
		
		try (
				BufferedReader brSistemas = new BufferedReader(new FileReader(Sistemas));
				BufferedReader brInformatica = new BufferedReader(new FileReader(Informatica));
				BufferedReader brmesclado  = new BufferedReader(new FileReader(mesclado)));
            ) {
			String lieasSistemas, lineaInformatica;
			
			while((lieasSistemas = brSistemas.readLine()) != null && (lineaInformatica = brInformatica.readLine()) != null ){
				mesclado.writer(lieasSistemas);
				mesclado.newLine();
				
				mesclado.writer(lineaInformatica);
				mesclado.newLine();
				
			}
			// solo de precausion
			
			// Si quedaron datos en sistemas
            while ((lineaSis = brSistemas.readLine()) != null) {
                bw.write(lineaSis);
                bw.newLine();
            }

            // Si quedaron datos en informática
            while ((lineaInf = brInformatica.readLine()) != null) {
                bw.write(lineaInf);
                bw.newLine();
            }
		}catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}