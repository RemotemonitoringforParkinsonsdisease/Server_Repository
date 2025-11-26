package POJOs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Signal {

    private Integer signalId;
    private Integer reportId;
    private SignalType signalType;
    private List<Integer> values;
    private Integer samplingRate;

    public Signal(Integer signalId, SignalType signalType) {
        this.signalId = signalId;
        this.signalType = signalType;
    }

    public Signal(SignalType signalType) {
        this.signalType = signalType;
        this.values = new ArrayList<>();
    }

    public Signal(SignalType signalType, Integer samplingRate, List<Integer> values) {
        this.signalType = signalType;
        this.values = values;
        this.samplingRate = samplingRate;
    }
    public Signal(Integer signalId, Integer reportId, SignalType signalType, List<Integer> values, Integer samplingRate) {
        this.signalId = signalId;
        this.reportId = reportId;
        this.signalType = signalType;
        this.values = values;
        this.samplingRate = samplingRate;
    }

    public Integer getSignalId() {
        return signalId;
    }

    public void setSignalId(Integer signalId) {
        this.signalId = signalId;
    }

    public SignalType getSignalType() {
        return signalType;
    }

    public void setSignalType(SignalType signalType) {
        this.signalType = signalType;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    public Integer getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(Integer samplingRate) {
        this.samplingRate = samplingRate;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String intValuesToString() {
        StringBuilder message = new StringBuilder();
        String separator = " ";

        for (int i = 0; i < values.size(); i++) {
            message.append(values.get(i));
            if (i < values.size() - 1) {
                message.append(separator);
            }
        }
        return message.toString();
    }

    public List<Integer> stringToIntValues(String str) {
        values.clear(); // Limpiamos la lista antes de agregar nuevos valores.
        String[] tokens = str.split(" "); // Dividimos el String por el espacio.
        int size = tokens.length;
        if(size>2) {
            for (int i = 0; i < size; i++) {
                try {
                    values.add(Integer.parseInt(tokens[i])); // Convertimos cada fragmento a Integer y lo agregamos a la ArrayList.
                } catch (NumberFormatException e) {
                    // Manejo de error si algún valor no es un Integer válido.
                    System.out.println("Error al convertir el valor: " + tokens[i]);
                }
            }
        }
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Signal signal = (Signal) o;
        return samplingRate == signal.samplingRate && Objects.equals(signalId, signal.signalId) && signalType == signal.signalType && Objects.equals(values, signal.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(signalId, signalType, values, samplingRate);
    }

    @Override
    public String toString() {
        return  signalType + " " + "[" + samplingRate + " Hz]";
    }


}
