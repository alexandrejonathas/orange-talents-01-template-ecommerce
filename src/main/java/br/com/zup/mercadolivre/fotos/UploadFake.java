package br.com.zup.mercadolivre.fotos;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadFake {

	public List<String> send(List<MultipartFile> files) {
		return files.stream().map(f -> 
			"http://fake.io/"+UUID.randomUUID()+"_"+f.getOriginalFilename()
		).collect(Collectors.toList());
	}
	
}
