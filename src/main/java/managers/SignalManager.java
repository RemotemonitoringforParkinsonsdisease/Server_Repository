package managers;

import POJOs.Signal;
import java.util.List;

public interface SignalManager {
    void addSignal(Signal signal);
    List<Signal> getSignalsByReport(Integer reportId);
}
