package filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import entidades.Pessoa;
import jpautil.JPAUtil;

@WebFilter(urlPatterns = {"/*"})
public class FilterAutenticacao implements Filter {
	
	@Inject
	private JPAUtil jpaUtil;

    public FilterAutenticacao() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();

		Pessoa userlogado = (Pessoa) session.getAttribute("usuarioLogado");
		
		String url = req.getServletPath();
		
		if(!url.equalsIgnoreCase("index.xhtml") && userlogado == null) {			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/login.xhtml");
			dispatcher.forward(request, response);
			return;//para o resto do codigo e redireciona
		}else {
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		jpaUtil.getEntityManager();
	}

}
