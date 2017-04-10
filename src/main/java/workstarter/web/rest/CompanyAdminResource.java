package workstarter.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.ApiParam;
import workstarter.repository.CompanyRepository;
import workstarter.repository.StudentRepository;
import workstarter.service.CompanyService;
import workstarter.service.MailService;
import workstarter.service.StudentService;
import workstarter.service.dto.CompanyAdminDTO;
import workstarter.service.dto.StudentDTO;
import workstarter.web.rest.util.PaginationUtil;

@RestController
@RequestMapping("/api")
public class CompanyAdminResource {

	   private final Logger log = LoggerFactory.getLogger(CompanyAdminResource.class);
	    private static final String ENTITY_NAME = "companyManagement";
	    private final CompanyRepository companyRepository;
	    private final MailService mailService;
	    private final CompanyService companyService;
	    
		public CompanyAdminResource(CompanyRepository companyRepository, MailService mailService,
				CompanyService companyService) {
			this.companyRepository = companyRepository;
			this.mailService = mailService;
			this.companyService = companyService;
		}

	    @GetMapping("/companies")
	    @Timed
	    public ResponseEntity<List<CompanyAdminDTO>> getAllUsers(@ApiParam Pageable pageable) {
	        final Page<CompanyAdminDTO> page = companyService.getAllManagedUsers(pageable);
	        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/companies");
	        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	    }
}
