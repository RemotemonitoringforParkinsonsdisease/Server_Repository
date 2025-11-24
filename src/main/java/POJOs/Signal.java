package POJOs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Signal {

    private Integer signalId;
    private Integer reportId;
    private SignalType signalType;
    private List<Integer> values;
    private Integer samplingRate;


    public Signal(Integer signalId, SignalType signalType, List<Integer> values, Integer samplingRate) {
        this.signalId = signalId;
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

    public String valuesToString() {
        StringBuilder sb = new StringBuilder();
        for (Float f : values) {
            sb.append(f).append(",");
        }
        if(sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // eliminar última coma
        }
        return sb.toString();
    }


    public void storeSignalInFile() {
        FileWriter fw = null;
        BufferedWriter bw = null;
        String ruta=null;
        try {
            if(this.signalType==SignalType.EDA) {
                ruta = "MeasurementsEDA\\" + signalFilename;

            }else{
                if(this.signalType==SignalType.EMG) {
                    ruta = "MeasurementsEMG\\" + signalFilename;
                }else{
                    if(this.signalType==SignalType.ECG) {
                        ruta = "MeasurementsECG\\" + signalFilename;
                    }else{
                        if(this.signalType==SignalType.ACC) {
                            ruta = "MeasurementsACC\\" + signalFilename;
                        }
                    }

                }
            }
            String contenido = getSignalValues(samplingRate).toString();
            File file = new File(ruta);
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(contenido);

        } catch (IOException ex) {
            Logger.getLogger(Signal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Signal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public LinkedList<Float> getSignalValues(int samplingRate) {
        LinkedList<Float> result = new LinkedList<>();
        for (int j = 0; j < samplingRate; j++) {
            int blockSize = samplingRate;
            // Si necesitas esta información visual, puedes guardarla en otro lugar.
            for (int i = 0; i < blockSize; i++) {
                int value = j * blockSize + i;
                result.add(values.get(value));  // Agregar los valores a la lista.
            }
        }
        return result;
    }


}
