package pages.ExtracaoDadosClimatologicos;

import lombok.Getter;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class INMET {
    private WebDriver driver;
    private NavegadorHelper navegadorHelper;

    String maxima = "//button[.='Maiores Temperaturas']";
    String minima = "//button[.='Menores Temperaturas ']";
    String precipitacao = "//button[.='Maiores Valores de Chuvas em 24h']";

    public INMET() {
        this.driver = DriverManager.getDriver();
        this.navegadorHelper = new NavegadorHelper(driver);
    }

    private void extrairTabelaPorData(String dia, String mes, String ano, String nomeArquivo) throws IOException {
        String dataCompleta = dia + "/" + mes + "/" + ano;

        navegadorHelper.esperarSegundos(2);
        navegadorHelper.clicarElementoPorXpath("//i[@class='bars icon header-icon']");
        navegadorHelper.esperarSegundos(2);
        navegadorHelper.preencherCampoPorXpath("//input[@type='date']", dataCompleta);
        navegadorHelper.esperarSegundos(2);
        navegadorHelper.clicarElementoPorXpath("//button[.='Gerar Tabela']");
        navegadorHelper.esperarSegundos(2);
        Assert.assertTrue(navegadorHelper.isElementPresent(By.xpath("//button[.='Instrução']")));

        String conteudo = (String) ((JavascriptExecutor) driver).executeScript(
                "let table = document.querySelector('table.ui.blue.unstackable.table');" +
                        "if (!table) return '';" +
                        "let rows = Array.from(table.querySelectorAll('tr')).slice(1);" +
                        "let result = '';" +
                        "rows.forEach(row => {" +
                        "   let cells = row.querySelectorAll('td');" +
                        "   cells.forEach(cell => result += cell.innerText + ' | ');" +
                        "   result += '\\n';" +
                        "});" +
                        "return result;"
        );

        BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo, true));
        writer.write(conteudo);
        writer.close();
    }

    public void prepararPagina(String tipo) {
        navegadorHelper.abrirURL("https://tempo.inmet.gov.br/TabelaEstacoes/A001");
        navegadorHelper.clicarElementoPorXpath("//i[@class='bars icon header-icon']");
        navegadorHelper.esperarSegundos(2);
        navegadorHelper.clicarElementoPorXpath("//input[@class='search']/..//div[@class='divider text']");
        navegadorHelper.esperarSegundos(2);
        navegadorHelper.clicarElementoPorXpath("//span[.='Valores Extremos']");
        navegadorHelper.esperarSegundos(2);
        navegadorHelper.clicarElementoPorXpath("//i[@class='bars icon header-icon']");
        navegadorHelper.esperarSegundos(2);
        navegadorHelper.clicarElementoPorXpath("//input[@class='search']/..//div[@class='divider default text']");
        navegadorHelper.esperarSegundos(2);
        navegadorHelper.clicarElementoPorXpath("//span[.='País']");
        navegadorHelper.esperarSegundos(2);
        navegadorHelper.clicarElementoPorXpath(tipo);
        navegadorHelper.esperarSegundos(2);
        navegadorHelper.clicarElementoPorXpath("//i[@class='bars icon header-icon']");
    }

    public void executarExtracao(List<Mes> listaMeses) {
        //String[] tipos = {"MAX", "MIN", "PREC"};
        //String[] dados = {maxima, minima, precipitacao};
        String[] tipos = {"MIN"};
        String[] dados = {minima};
        for (int i = 0; i < tipos.length; i++) {
            String tipoXpath = dados[i];
            String tipoLabel = tipos[i];

            prepararPagina(tipoXpath); // Só uma vez por tipo

            for (int ano = 2000; ano <= 2001; ano++) {
                for (Mes mes : listaMeses) {
                    for (int dia = 1; dia <= mes.dias; dia++) {
                        String sDia = String.format("%02d", dia);
                        String sMes = mes.numero;
                        String data = sDia + "/" + sMes + "/" + ano;
                        String nomeArquivo = tipoLabel + "-" + mes.nome + "-" + ano + ".txt";

                        boolean sucesso = false;
                        int tentativas = 0;

                        while (!sucesso && tentativas < 100) {
                            try {
                                extrairTabelaPorData(sDia, sMes, String.valueOf(ano), nomeArquivo);
                                sucesso = true;
                            } catch (Throwable t) {
                                tentativas++;
                                System.out.println("Erro em " + tipoLabel + " - " + data + " (tentativa " + tentativas + ")");

                            }
                        }

                        if (!sucesso) {
                            System.out.println("❌ Falhou definitivamente em " + tipoLabel + " - " + data);
                        }
                    }
                }
            }
        }
    }

    public List<Mes> filtrarMesesDesejados(List<Mes> todosOsMeses, String[] nomesDesejados) {
        List<Mes> filtrados = new ArrayList<>();
        for (Mes mes : todosOsMeses) {
            for (String nome : nomesDesejados) {
                if (mes.nome.equalsIgnoreCase(nome)) {
                    filtrados.add(mes);
                    break; // Evita comparar com os demais nomes
                }
            }
        }
        return filtrados;
    }

    public static void main(String[] args) throws IOException {
        INMET inmet = new INMET();
        Meses meses = new Meses();
        List<Mes> todosOsMeses = meses.getListaDeMeses();

        //String[] nomesDesejados = {"JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV", "DEZ"};
        String[] nomesDesejados = {"JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV", "DEZ"};
        List<Mes> mesesFiltrados = inmet.filtrarMesesDesejados(todosOsMeses, nomesDesejados);

        inmet.executarExtracao(mesesFiltrados);
    }
}
