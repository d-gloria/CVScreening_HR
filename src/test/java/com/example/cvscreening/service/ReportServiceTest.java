package com.example.cvscreening.service;

import com.example.cvscreening.service.fake.FakeRaportDao;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReportServiceTest {

    @Test
    void generateReport_success_defaultsFormatToPdf() {
        FakeRaportDao dao = new FakeRaportDao();
        ReportService service = new ReportService(dao);

        var idOpt = service.gjeneroRaport(5, "KPI", "Raport mujor", "");
        assertThat(idOpt).isPresent();

        var raport = service.merrRaport(idOpt.get()).orElseThrow();
        assertThat(raport.getIdRekrutues()).isEqualTo(5);
        assertThat(raport.getLlojiRaportit()).isEqualTo("KPI");
        assertThat(raport.getFormatiRaportit()).isEqualTo("PDF");
    }

    @Test
    void getReportsByRecruiter_returnsList() {
        FakeRaportDao dao = new FakeRaportDao();
        ReportService service = new ReportService(dao);

        service.gjeneroRaport(7, "KPI", "R1", "PDF");
        service.gjeneroRaport(7, "STATISTIKA", "R2", "XLSX");
        service.gjeneroRaport(8, "KPI", "Other", "PDF");

        var list = service.merrRaporte(7);
        assertThat(list).hasSize(2);
        assertThat(list.get(0).getIdRekrutues()).isEqualTo(7);
    }

    @Test
    void generateReport_fails_whenMissingType() {
        FakeRaportDao dao = new FakeRaportDao();
        ReportService service = new ReportService(dao);

        var idOpt = service.gjeneroRaport(5, "   ", "Raport", "PDF");
        assertThat(idOpt).isEmpty();
    }
}
