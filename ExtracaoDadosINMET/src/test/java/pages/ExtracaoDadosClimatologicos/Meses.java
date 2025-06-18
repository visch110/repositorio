package pages.ExtracaoDadosClimatologicos;

import java.util.ArrayList;
import java.util.List;

public class Meses {

    public List<Mes> getListaDeMeses() {
        List<Mes> lista = new ArrayList<>();

        lista.add(new Mes("JAN", "01", 31));
        lista.add(new Mes("FEV", "02", 28)); // ou 29 em ano bissexto
        lista.add(new Mes("MAR", "03", 31));
        lista.add(new Mes("ABR", "04", 30));
        lista.add(new Mes("MAI", "05", 31));
        lista.add(new Mes("JUN", "06", 30));
        lista.add(new Mes("JUL", "07", 31));
        lista.add(new Mes("AGO", "08", 31));
        lista.add(new Mes("SET", "09", 30));
        lista.add(new Mes("OUT", "10", 31));
        lista.add(new Mes("NOV", "11", 30));
        lista.add(new Mes("DEZ", "12", 31));

        return lista;
    }
}
