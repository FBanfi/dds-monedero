package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;

public class ValidadorService {
    private final static ValidadorService INSTANCE = new ValidadorService();

    private ValidadorService() {
    }

    public static ValidadorService getInstance() {
        return INSTANCE;
    }

    public void validarPoner(Cuenta cuenta, double cuanto) {
        this.validarMontoNegativo(cuanto);
        this.validarCantidadDepositos(cuenta);

    }

    public void validarSacar(Cuenta cuenta, double cuanto) {
        this.validarMontoNegativo(cuanto);
        this.validarSaldo(cuenta, cuanto);
        this.validarLimite(cuenta, cuanto);
    }

    private void validarCantidadDepositos(Cuenta cuenta) {
        if (cuenta.getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
            throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
        }
    }

    public void validarMontoNegativo(double cuanto) {
        if (cuanto <= 0) {
            throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
        }
    }

    private void validarSaldo(Cuenta cuenta, double cuanto) {
        if (cuenta.getSaldo() - cuanto < 0) {
            throw new SaldoMenorException("No puede sacar mas de " + cuenta.getSaldo() + " $");
        }
    }

    private void validarLimite(Cuenta cuenta, double cuanto) {
        double limite = this.calcularLimite(cuenta);
        if (cuanto > limite) {
            throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
                    + " diarios, límite: " + limite);
        }
    }

    public double calcularLimite(Cuenta cuenta) {
        return 1000 - cuenta.getMontoExtraidoA(LocalDate.now());
    }
}
