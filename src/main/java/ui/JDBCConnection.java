package ui;

import jdbcs.*;
//TODO: Sobra

public class JDBCConnection {
    private DoctorJDBC doctorJDBC;
    private PatientJDBC patientJDBC;
    private ReportJDBC reportJDBC;
    private SignalJDBC signalJDBC;

    public JDBCConnection(ManagerJDBC manager) {
        this.doctorJDBC = new DoctorJDBC(manager);
        this.patientJDBC = new PatientJDBC(manager);
        this.reportJDBC = new ReportJDBC(manager);
        this.signalJDBC = new SignalJDBC(manager);
    }

    public DoctorJDBC getDoctorJDBC() {
        return doctorJDBC;
    }

    public PatientJDBC getPatientJDBC() {
        return patientJDBC;
    }

    public ReportJDBC getReportJDBC() {
        return reportJDBC;
    }

    public SignalJDBC getSignalJDBC() {
        return signalJDBC;
    }
}
