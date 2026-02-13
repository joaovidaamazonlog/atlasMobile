# Atlas Android App

## Visão Geral
Este é o aplicativo Android nativo para o sistema **Atlas**, projetado para visualização e gestão geoespacial de parceiros logísticos. O app consome dados estáticos diretamente do GitHub Pages, mantendo a arquitetura descentralizada e sem backend próprio.

## Arquitetura
O projeto segue os princípios de **Clean Architecture** e o padrão **MVVM**:

- **Data Layer**: Responsável pelo consumo da API (Retrofit) e cache local (Room).
- **Domain Layer**: Contém as entidades de negócio e casos de uso, independente de frameworks.
- **Presentation Layer**: Implementada com **Jetpack Compose** para uma UI moderna e reativa, utilizando ViewModels para gestão de estado.

## Tecnologias Utilizadas
- **Kotlin**: Linguagem principal.
- **Jetpack Compose**: UI declarativa.
- **Google Maps SDK**: Renderização de mapas e GeoJSON.
- **Retrofit + Moshi**: Consumo de dados JSON.
- **Coroutines + Flow**: Processamento assíncrono.
- **Hilt**: Injeção de dependência.

## Funcionalidades
- Visualização de parceiros em tempo real.
- Camadas de GeoJSON (Clusters, Gaps, Jurisdição).
- Filtros por Delivery Station e Status.
- Métrica de **Dificuldade de Prospecção** integrada.
- Suporte Offline via cache local.

## Como Rodar
1. Clone o repositório.
2. Abra o projeto no **Android Studio Hedgehog** ou superior.
3. Adicione sua `MAPS_API_KEY` no arquivo `local.properties`.
4. Sincronize o Gradle e execute no emulador ou dispositivo físico.

## Evolução Futura
- Implementação de notificações push para novos GAPs identificados.
- Modo de navegação offline aprimorado.
- Integração com Salesforce via Deep Links.
