package truck.project.core.reporting

import truck.project.features.fleet.domain.model.Truck
import truck.project.features.fleet.domain.model.Driver
import truck.project.features.trips.data.local.TripEntity
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import truck.project.Platform

class ReportServiceImpl(private val platform: Platform) : ReportService {

    private fun getHeader(title: String): String {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val dateStr = "${now.dayOfMonth}/${now.monthNumber}/${now.year}"
        val timeStr = "${now.hour}:${now.minute.toString().padStart(2, '0')}"
        val logoBase64 = platform.getLogoBase64()
        
        return """
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; color: #1a1a1a; margin: 0; padding: 0; background-color: #f4f7f6; }
                    .top-stripe { height: 12px; background: linear-gradient(90deg, #001E4D 0%, #FACC15 100%); }
                    .header { background: white; padding: 40px 60px; border-bottom: 1px solid #e1e4e8; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
                    .brand-box { display: flex; align-items: center; }
                    .logo-img { width: 100px; height: 100px; object-fit: contain; margin-right: 25px; filter: drop-shadow(0 4px 6px rgba(0,0,0,0.1)); }
                    .brand-text-container { border-left: 3px solid #FACC15; padding-left: 20px; }
                    .app-name { font-size: 36px; font-weight: 900; color: #001E4D; margin: 0; letter-spacing: -1px; }
                    .app-tagline { font-size: 13px; color: #666; font-weight: 600; letter-spacing: 4px; text-transform: uppercase; margin-top: 2px; }
                    
                    .meta-info { text-align: right; }
                    .title-label { font-size: 24px; font-weight: 800; color: #001E4D; margin-bottom: 8px; text-transform: uppercase; }
                    .date-label { font-size: 14px; color: #777; font-weight: 500; }
                    
                    .main-content { padding: 50px 60px; max-width: 1100px; margin: 0 auto; }
                    
                    .stats-row { display: flex; gap: 30px; margin-bottom: 50px; }
                    .stat-card { flex: 1; background: white; padding: 30px; border-radius: 24px; box-shadow: 0 15px 35px rgba(0,0,0,0.08); border: 1px solid rgba(0,0,0,0.03); position: relative; }
                    .stat-card:after { content: ""; position: absolute; bottom: 0; left: 30px; right: 30px; height: 4px; border-radius: 2px; background: #eee; }
                    .stat-card.primary:after { background: #001E4D; }
                    .stat-card.accent:after { background: #FACC15; }
                    .stat-card.success:after { background: #22c55e; }
                    
                    .stat-val { font-size: 42px; font-weight: 900; color: #001E4D; margin-bottom: 5px; }
                    .stat-lbl { font-size: 14px; color: #999; font-weight: 700; text-transform: uppercase; letter-spacing: 1.5px; }
                    
                    .table-wrapper { background: white; border-radius: 24px; overflow: hidden; box-shadow: 0 20px 40px rgba(0,0,0,0.06); border: 1px solid #eee; }
                    .table-head { background: #001E4D; color: white; padding: 25px 35px; display: flex; justify-content: space-between; align-items: center; }
                    .table-head-title { font-size: 18px; font-weight: 700; }
                    
                    table { width: 100%; border-collapse: collapse; }
                    th { text-align: left; padding: 20px 35px; font-size: 13px; color: #bbb; text-transform: uppercase; border-bottom: 1px solid #f0f0f0; letter-spacing: 1px; }
                    td { padding: 22px 35px; font-size: 15px; border-bottom: 1px solid #f9f9f9; color: #333; }
                    tr:last-child td { border-bottom: none; }
                    tr:nth-child(even) { background: #fafafa; }
                    
                    .status-pill { padding: 8px 16px; border-radius: 50px; font-size: 12px; font-weight: 800; text-transform: uppercase; display: inline-block; }
                    .status-ok { background: #e8f5e9; color: #2e7d32; }
                    .status-wait { background: #fff3e0; color: #e65100; }
                    .status-action { background: #e3f2fd; color: #1565c0; }
                    
                    .report-footer { padding: 60px; text-align: center; color: #aaa; font-size: 12px; }
                    .legal-notice { font-style: italic; margin-top: 10px; }
                </style>
            </head>
            <body>
                <div class="top-stripe"></div>
                <div class="header">
                    <div class="brand-box">
                        ${if (logoBase64.isNotEmpty()) "<img class=\"logo-img\" src=\"data:image/png;base64,$logoBase64\" alt=\"Logo\">" else "<div style=\"width:80px;height:80px;background:#001E4D;border-radius:20%;margin-right:20px;\"></div>"}
                        <div class="brand-text-container">
                            <h1 class="app-name">TRUCK APP</h1>
                            <div class="app-tagline">Soluciones Logísticas Volvo</div>
                        </div>
                    </div>
                    <div class="meta-info">
                        <div class="title-label">$title</div>
                        <div class="date-label">Emitido el <b>$dateStr</b> a las <b>$timeStr</b></div>
                    </div>
                </div>
                <div class="main-content">
        """.trimIndent()
    }

    private fun getFooter(): String {
        val year = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
        return """
                </div>
                <div class="report-footer">
                    <div style="font-weight:bold; color:#001E4D; margin-bottom:5px;">TRUCK APP LOGISTICS SYSTEM</div>
                    <div>© $year Todos los derechos reservados • Reporte de caracter confidencial</div>
                    <div class="legal-notice">Generado automáticamente por la plataforma de gestión de flota Truck App.</div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }

    override suspend fun generateTripsReport(trips: List<TripEntity>): Result<ReportResult> {
        val completed = trips.count { it.status == "COMPLETED" }
        val enRoute = trips.count { it.status == "EN_ROUTE" }
        
        val body = """
            <div class="stats-row">
                <div class="stat-card primary"><div class="stat-val">${trips.size}</div><div class="stat-lbl">Viajes Totales</div></div>
                <div class="stat-card success"><div class="stat-val">$completed</div><div class="stat-lbl">Finalizados</div></div>
                <div class="stat-card accent"><div class="stat-val">$enRoute</div><div class="stat-lbl">En Tránsito</div></div>
            </div>
            
            <div class="table-wrapper">
                <div class="table-head">
                    <div class="table-head-title">Detalle Maestro de Viajes</div>
                    <div style="font-size:12px; opacity:0.8;">${trips.size} registros encontrados</div>
                </div>
                <table>
                    <thead>
                        <tr><th>Referencia</th><th>Origen</th><th>Destino</th><th>Estado de Misión</th></tr>
                    </thead>
                    <tbody>
                        ${trips.joinToString("") { 
                            val (css, label) = when(it.status) {
                                "COMPLETED" -> "status-ok" to "FINALIZADO"
                                "EN_ROUTE" -> "status-action" to "EN RUTA"
                                "PROGRAMMED" -> "status-action" to "PROGRAMADO"
                                else -> "status-wait" to "PLANIFICADO"
                            }
                            "<tr><td><b>#${it.id.takeLast(6).uppercase()}</b></td><td>${it.origin}</td><td>${it.destination}</td><td><span class=\"status-pill $css\">$label</span></td></tr>" 
                        }}
                    </tbody>
                </table>
            </div>
        """.trimIndent()
        
        val html = getHeader("Reporte Maestro de Operaciones") + body + getFooter()
        val fileName = "Reporte_Rutas_TruckApp_${Clock.System.now().toEpochMilliseconds()}.html"
        val path = "${platform.downloadFolder}/$fileName"
        platform.writeFile(path, html)
        
        return Result.success(ReportResult(path, "Reporte de operaciones logísticas generado."))
    }

    override suspend fun generateFleetReport(trucks: List<Truck>): Result<ReportResult> {
        val active = trucks.count { it.status.name == "AVAILABLE" }
        val totalCap = trucks.sumOf { it.capacity }
        
        val body = """
            <div class="stats-row">
                <div class="stat-card primary"><div class="stat-val">${trucks.size}</div><div class="stat-lbl">Unidades</div></div>
                <div class="stat-card success"><div class="stat-val">$active</div><div class="stat-lbl">Disponibles</div></div>
                <div class="stat-card accent"><div class="stat-val">${totalCap}tn</div><div class="stat-lbl">Carga Total</div></div>
            </div>
            
            <div class="table-wrapper">
                <div class="table-head">
                    <div class="table-head-title">Inventario de Activos (Flota Volvo)</div>
                    <div style="font-size:12px; opacity:0.8;">Actualizado en tiempo real</div>
                </div>
                <table>
                    <thead>
                        <tr><th>Placa / Matrícula</th><th>Modelo Volvo</th><th>Capacidad</th><th>Estatus</th></tr>
                    </thead>
                    <tbody>
                        ${trucks.joinToString("") { 
                            val (css, label) = when(it.status.name) {
                                "AVAILABLE" -> "status-ok" to "DISPONIBLE"
                                "IN_ROUTE" -> "status-action" to "EN SERVICIO"
                                else -> "status-wait" to "MANTENIMIENTO"
                            }
                            "<tr><td><b style=\"font-size:16px;\">${it.plateNumber.value}</b></td><td>${it.model}</td><td>${it.capacity} TN</td><td><span class=\"status-pill $css\">$label</span></td></tr>" 
                        }}
                    </tbody>
                </table>
            </div>
        """.trimIndent()
        
        val html = getHeader("Reporte de Inventario de Flota") + body + getFooter()
        val fileName = "Reporte_Flota_TruckApp_${Clock.System.now().toEpochMilliseconds()}.html"
        val path = "${platform.downloadFolder}/$fileName"
        platform.writeFile(path, html)
        
        return Result.success(ReportResult(path, "Reporte de inventario de flota generado con éxito."))
    }

    override suspend fun generateDriversReport(drivers: List<Driver>): Result<ReportResult> {
        val online = drivers.count { it.isOnline }
        
        val body = """
            <div class="stats-row">
                <div class="stat-card primary"><div class="stat-val">${drivers.size}</div><div class="stat-label">Total Operadores</div></div>
                <div class="stat-card success"><div class="stat-val">$online</div><div class="stat-label">Conectados</div></div>
                <div class="stat-card accent"><div class="stat-val">${drivers.size - online}</div><div class="stat-label">Desconectados</div></div>
            </div>
            
            <div class="table-wrapper">
                <div class="table-head">
                    <div class="table-head-title">Nómina Corporativa de Conductores</div>
                    <div style="font-size:12px; opacity:0.8;">Gestión de Talento Humano</div>
                </div>
                <table>
                    <thead>
                        <tr><th>Operador Logístico</th><th>Documento C.I.</th><th>Licencia</th><th>Estado</th></tr>
                    </thead>
                    <tbody>
                        ${drivers.joinToString("") { 
                            "<tr><td><b>${it.name}</b></td><td>${it.dni}</td><td>${it.licenseNumber}</td><td><span class=\"status-pill ${if (it.isOnline) "status-ok" else "status-wait"}\">${if (it.isOnline) "EN LÍNEA" else "OFFLINE"}</span></td></tr>" 
                        }}
                    </tbody>
                </table>
            </div>
        """.trimIndent()

        val html = getHeader("Reporte de Personal de Operaciones") + body + getFooter()
        val fileName = "Reporte_Personal_TruckApp_${Clock.System.now().toEpochMilliseconds()}.html"
        val path = "${platform.downloadFolder}/$fileName"
        platform.writeFile(path, html)
        
        return Result.success(ReportResult(path, "Reporte de personal generado con éxito."))
    }
}
