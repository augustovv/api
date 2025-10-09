package com.hortanaporta.api.services;

import com.hortanaporta.api.model.Endereco;
import com.hortanaporta.api.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    private final String VIA_CEP_URL = "https://viacep.com.br/ws/";

    // Buscar endereço por CEP na ViaCEP
    public Endereco buscarPorCep(String cep) {
        String cepLimpo = cep.replaceAll("\\D", "");
        String url = VIA_CEP_URL + cepLimpo + "/json/";
        
        RestTemplate restTemplate = new RestTemplate();
        try {
            ViaCepResponse viaCepResponse = restTemplate.getForObject(url, ViaCepResponse.class);
            
            if (viaCepResponse != null && !viaCepResponse.getErro()) {
                Endereco endereco = new Endereco();
                endereco.setCep(viaCepResponse.getCep());
                endereco.setLogradouro(viaCepResponse.getLogradouro());
                endereco.setBairro(viaCepResponse.getBairro());
                endereco.setCidade(viaCepResponse.getLocalidade());
                endereco.setEstado(viaCepResponse.getUf());
                endereco.setComplemento(viaCepResponse.getComplemento());
                
                return endereco;
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar CEP: " + e.getMessage());
        }
        
        return null;
    }

    // Salvar endereço no banco
    public Endereco salvarEndereco(Endereco endereco) {
        // Se for o primeiro endereço da pessoa, definir como principal
        if (endereco.getCdPessoa() != null) {
            List<Endereco> enderecosExistentes = enderecoRepository.findByCdPessoa(endereco.getCdPessoa());
            if (enderecosExistentes.isEmpty()) {
                endereco.setEnderecoPrincipal(true);
            }
        }
        
        return enderecoRepository.save(endereco);
    }

    // Buscar endereços por pessoa
    public List<Endereco> buscarEnderecosPorPessoa(Long cdPessoa) {
        return enderecoRepository.findByCdPessoa(cdPessoa);
    }

    // Buscar endereço principal da pessoa
    public Optional<Endereco> buscarEnderecoPrincipal(Long cdPessoa) {
        return enderecoRepository.findByCdPessoaAndEnderecoPrincipalTrue(cdPessoa);
    }

    // Definir endereço como principal
    public Endereco definirEnderecoPrincipal(Long cdEndereco, Long cdPessoa) {
        // Remover principal de todos os endereços da pessoa
        List<Endereco> enderecos = enderecoRepository.findByCdPessoa(cdPessoa);
        for (Endereco endereco : enderecos) {
            if (endereco.getEnderecoPrincipal()) {
                endereco.setEnderecoPrincipal(false);
                enderecoRepository.save(endereco);
            }
        }
        
        // Definir novo endereço como principal
        Optional<Endereco> enderecoOpt = enderecoRepository.findById(cdEndereco);
        if (enderecoOpt.isPresent()) {
            Endereco endereco = enderecoOpt.get();
            endereco.setEnderecoPrincipal(true);
            return enderecoRepository.save(endereco);
        }
        
        return null;
    }

    // Deletar endereço
    public boolean deletarEndereco(Long cdEndereco) {
        if (enderecoRepository.existsById(cdEndereco)) {
            enderecoRepository.deleteById(cdEndereco);
            return true;
        }
        return false;
    }

    // Classe interna para mapear resposta da ViaCEP
    public static class ViaCepResponse {
        private String cep;
        private String logradouro;
        private String complemento;
        private String bairro;
        private String localidade;
        private String uf;
        private boolean erro;

        // Getters e Setters
        public String getCep() { return cep; }
        public void setCep(String cep) { this.cep = cep; }

        public String getLogradouro() { return logradouro; }
        public void setLogradouro(String logradouro) { this.logradouro = logradouro; }

        public String getComplemento() { return complemento; }
        public void setComplemento(String complemento) { this.complemento = complemento; }

        public String getBairro() { return bairro; }
        public void setBairro(String bairro) { this.bairro = bairro; }

        public String getLocalidade() { return localidade; }
        public void setLocalidade(String localidade) { this.localidade = localidade; }

        public String getUf() { return uf; }
        public void setUf(String uf) { this.uf = uf; }

        public boolean getErro() { return erro; }
        public void setErro(boolean erro) { this.erro = erro; }
    }
}