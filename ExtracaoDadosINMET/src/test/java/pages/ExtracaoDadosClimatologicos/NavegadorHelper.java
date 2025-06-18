package pages.ExtracaoDadosClimatologicos;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class NavegadorHelper {
    private WebDriver driver;

    // Construtor para receber o driver WebDriver
    public NavegadorHelper(WebDriver driver) {
        this.driver = driver;
    }

    // Método para realizar ações de interação com o navegador
    public void abrirURL(String URL) {
        // Exemplo de ação: abrir uma URL
        driver.get(URL);
    }

    public void preencherCampoPorId(String id, String texto) {
        WebElement elemento = driver.findElement(By.id(id));
        elemento.sendKeys(texto);
    }

    public void preencherCampoPorXpath(String xpath, String texto) {
        WebElement elemento = driver.findElement(By.xpath(xpath));
        elemento.sendKeys(texto);
    }

    public void clicarElementoPorId(String id) {
        WebElement elemento = driver.findElement(By.id(id));
        elemento.click();
    }

    public void clicarElementoPorXpath(String xpath) {
        WebElement elemento = driver.findElement(By.xpath(xpath));
        elemento.click();
    }



    public boolean compararTextoElementoComTextoEsperado(By locator, String textoEsperado) {
        try {
            WebElement elemento = driver.findElement(locator);
            String textoElemento = elemento.getText();
            System.out.println("Texto obtido do elemento: " + textoElemento);
            return textoElemento.equals(textoEsperado);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String obterTextoDoElemento(By locator) {
        try {
            WebElement elemento = driver.findElement(locator);
            return elemento.getText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void fecharDriver() {
        if (driver != null) {
            driver.close();
            //driver.quit();
            driver = null; // Liberando o driver da memória
        }
    }

    public void esperarSegundos(int seconds) {
        try {
            Thread.sleep(seconds * 1000); // Converte segundos em milissegundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }





    public boolean isElementPresent(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            return !elements.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}