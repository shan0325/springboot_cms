package com.shan.app.service.admin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.shan.app.domain.Code;
import com.shan.app.error.CodeDuplicatedException;
import com.shan.app.repository.admin.AdminCodeRepository;
import com.shan.app.service.admin.dto.CodeDTO;

@Service
@Transactional
public class AdminCodeService {
	
	@Resource(name="adminCodeRepository")
	private AdminCodeRepository adminCodeRepository;

	public Code createCode(@Valid CodeDTO.Create create) {
		
		Optional<Code> codeOptional = adminCodeRepository.findByCode(create.getCode());
		if(codeOptional.isPresent()) {
			throw new CodeDuplicatedException(create.getCode());
		}
		
		if(!StringUtils.isEmpty(create.getParentCode())) {
			Optional<Code> parCodeOptional = adminCodeRepository.findByCode(create.getParentCode());
			Code parCode = parCodeOptional.orElseThrow(() -> new CodeDuplicatedException(create.getParentCode()));
			
			List<Code> codes = adminCodeRepository.findByParentCode(create.getParentCode());
			Integer maxOrd = codes.stream()
								.mapToInt(Code::getOrd)
								.max()
								.orElse(0);
			
			Code code = new Code();
			code.setCode(create.getCode());
			code.setCodeName(create.getCodeName());
			code.setCodeDesc(create.getCodeDesc());
			code.setParentCode(create.getParentCode());
			code.setTopCode(parCode.getTopCode());
			code.setLevel(parCode.getLevel() + 1);
			code.setOrd(maxOrd + 1);
			code.setUseYn("Y");
			code.setRegDate(LocalDateTime.now());
			
			return adminCodeRepository.save(code);
		} else {
			List<Code> codes = adminCodeRepository.findByLevel(1);
			Integer maxOrd = codes.stream()
								.mapToInt(Code::getOrd)
								.max()
								.orElse(0);
			
			Code code = new Code();
			code.setCode(create.getCode());
			code.setCodeName(create.getCodeName());
			code.setCodeDesc(create.getCodeDesc());
			code.setParentCode(create.getParentCode());
			code.setTopCode(create.getCode());
			code.setLevel(1);
			code.setOrd(maxOrd + 1);
			code.setUseYn("Y");
			code.setRegDate(LocalDateTime.now());
			
			return adminCodeRepository.save(code);
		}
	}

	
}
