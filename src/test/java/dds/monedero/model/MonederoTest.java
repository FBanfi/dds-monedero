package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void Poner() {
    Assertions.assertDoesNotThrow(() -> cuenta.poner(1500));
  }

  @Test
  void PonerMontoNegativoTiraUnaExcepcion() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void TresDepositosConMontosCorrectosSeSumanAlSaldoDeLaCuenta() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    Assertions.assertEquals(cuenta.getSaldo(), 3856);
  }

  @Test
  void HacerMasDeTresDepositosEnUnDiaTiraExcepcion() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
  }

  @Test
  void ExtraerMasQueElSaldoTiraExcepcion() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMasDe1000TiraExcepcion() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativoTiraExcepcion() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

}