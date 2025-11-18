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

    private String signalId;
    private SignalType signalType;
    private List<Float> values;
    private final int samplingRate = 100;
    private String signalFilename;


    public Signal(SignalType signalType, String signalId){
        this.values = new LinkedList<>();
        this.signalId = signalId;
        this.signalType = signalType;
    }

    public String getSignalId() {
        return signalId;
    }

    public void setSignalId(String signalId) {
        this.signalId = signalId;
    }

    public SignalType getSignalType() {
        return signalType;
    }

    public void setSignalType(SignalType signalType) {
        this.signalType = signalType;
    }

    public List<Float> getValues() {
        return values;
    }

    public void setValues(List<Float> values) {
        this.values = values;
    }

    public int getSamplingRate() {
        return samplingRate;
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
