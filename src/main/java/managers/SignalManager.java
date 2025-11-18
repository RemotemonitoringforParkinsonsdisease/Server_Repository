package managers;

import POJOs.Signal;
import POJOs.SignalType;

public interface SignalManager {
    void addSignal(Signal signal);
    Signal getSignalById(String signalId);
}
