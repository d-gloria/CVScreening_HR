package com.example.cvscreening;

import com.example.cvscreening.config.Db;
import com.example.cvscreening.dao.*;
import com.example.cvscreening.dao.jdbc.*;
import com.example.cvscreening.service.*;
import com.example.cvscreening.ui.ConsoleUI;

import java.sql.Connection;

public class App {

    public static void main(String[] args) throws Exception {
        try (Connection conn = Db.connect()) {
            Db.runSchema(conn);

            // DAOs
            PerdoruesDao perdoruesDao = new PerdoruesDaoJdbc(conn);
            KandidatDao kandidatDao = new KandidatDaoJdbc(conn);
            CvDao cvDao = new CvDaoJdbc(conn);
            PozicionPuneDao pozicionDao = new PozicionPuneDaoJdbc(conn);
            AplikimDao aplikimDao = new AplikimDaoJdbc(conn);
            IntervisteDao intervisteDao = new IntervisteDaoJdbc(conn);
            NjoftimDao njoftimDao = new NjoftimDaoJdbc(conn);
            RaportDao raportDao = new RaportDaoJdbc(conn);
            RegjisterAuditiDao auditDao = new RegjisterAuditiDaoJdbc(conn);

            // Services
            AuthService authService = new AuthService(perdoruesDao);

            ApplicationService applicationService = new ApplicationService(
                    kandidatDao, cvDao, pozicionDao, aplikimDao, auditDao, njoftimDao
            );

            InterviewService interviewService = new InterviewService(
                    intervisteDao, aplikimDao, kandidatDao, auditDao, njoftimDao
            );

            NotificationService notificationService = new NotificationService(njoftimDao);

            ReportService reportService = new ReportService(raportDao);

            ScreeningService screeningService = new ScreeningService(
                    kandidatDao, cvDao, pozicionDao, aplikimDao, auditDao, njoftimDao
            );

            // UI
            ConsoleUI ui = new ConsoleUI(
                    authService,
                    applicationService,
                    screeningService,
                    interviewService,
                    notificationService,
                    reportService,
                    perdoruesDao,
                    kandidatDao,
                    cvDao,
                    pozicionDao,
                    aplikimDao
            );

            ui.run();
        }
    }
}
