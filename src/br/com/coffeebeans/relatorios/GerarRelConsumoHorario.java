package br.com.coffeebeans.relatorios;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Calendar;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;
import br.com.coffeebeans.util.Conexao;

@WebServlet("/GerarRelConsumoHorario")
public class GerarRelConsumoHorario extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Timestamp dataIniStamp;
	private String dataIniString;
	private Timestamp dataFimStamp;
	private String dataFimString;
	private Connection conectar;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {

			dataIniString = request.getParameter("hora-inicial");
			dataFimString = request.getParameter("hora-final");
			
			dataIniString=dataIniString.replace("T"," ");
			dataFimString=dataFimString.replace("T"," ");
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			sdf.setLenient(false);

			Calendar c = Calendar.getInstance();

			c.setTime(sdf.parse(dataIniString));
			dataIniStamp = new Timestamp(c.getTimeInMillis());

			c.setTime(sdf.parse(dataFimString));
			dataFimStamp = new Timestamp(c.getTimeInMillis());

			
			conectar = Conexao.conectar("mysql");

			//System.out.println(dataIniStamp);
			ServletContext context = getServletContext();
			byte[] bytes = null;

			// carrega o arquivo jasper

			JasperReport relatorioJasper = (JasperReport) JRLoader
					.loadObjectFromFile(context
							.getRealPath("/WEB-INF/RelConsumoHorario.jasper"));

			// Na variavel pathJasper ficara o caminho do diret�rio para
			// os relat�rios compilados (.jasper)
			String pathJasper = getServletContext().getRealPath("/WEB-INF/")
					+ "/";

			// A variavel path armazena o caminho real para o contexto
			// isso � util pois o seu web container pode estar instalado em
			// lugares diferentes
			String path = getServletContext().getRealPath("/");

			// par�metros
			HashMap<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("dtHoraIni", dataIniStamp);
			parametros.put("dtHoraFim", dataFimStamp);

			// direciona a sa�da do relat�rio para um stream
			bytes = JasperRunManager.runReportToPdf(relatorioJasper,
					parametros, conectar);

			// System.out.println(path);

			if (bytes != null && bytes.length > 0) {

				// envia o relat�rio em formato PDF para o browser
				response.setContentType("application/pdf");

				response.setContentLength(bytes.length);
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(bytes, 0, bytes.length);
				ouputStream.flush();
				ouputStream.close();

			}
			
			/*System.out.println(relatorioJasper.getQuery().getText().toString());
			System.out.println(dataIniStamp);
			System.out.println(dataFimStamp); */

			/*
			 * catch (JRException e) { e.printStackTrace(); e.getCause();
			 * e.getStackTrace(); System.out.println(e.getMessage()); }
			 */

		} catch (Exception e) {
			e.printStackTrace();
			e.getCause();
			e.getStackTrace();
			System.out.println(e.getMessage());

			PrintWriter out = response.getWriter();
			out.print("<html>");
			out.print("<body>");
			out.print("<script type=\"text/javascript\">");
			out.print("alert(\"N�o foi poss�vel gerar o relat�rio.\");");
			out.print("</script>");
			out.print(e.getMessage());
			out.print("</body>");
			out.print("</html>");
		} finally {

			try {
				conectar.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
