# MusicApp

### Explicação do Sistema

O domínio do MusicApp é composto por 5 contextos: 

* Catálogo;
* Plataforma;
* Biblioteca;
* Usuário;
* Pagamento.

Cada um destes contextos é um microserviço.

Primeiramente, o Catálogo é um contexto central do negócio, por gerenciar artistas, músicas, albúms, e etc. Ele vai se comunicar com a plataforma e a biblioteca, porém são dois tipos de relacionamentos diferentes.

Plataforma é conformista de acordo com o Catálogo, se ajustando e seguindo a cada mudança realizada nela. Catálogo e Biblioteca (conectada com o Usuário) trabalham em conjunto para disponibilizar o acervo de músicas para o usuário final, de modo personalizado.

O contexto de Usuário se comunica com a API ACL de Pagamento, que então se comunica com o gateway externo para realizar os pagamentos e proteger os demais contextos do ambiente externo, não controlado, e que pode não seguir a linguagem ubíqua definida pela empresa.

### Mapa de Contexto Estratégico

<img src="./dominio.jpg" />
