# Studio (Fabric 1.20.1)

- **Ctrl + O**: teletransporta instantaneamente (sem tela de carregamento) para a dimensao `studio`.
- Apertar de novo volta para a dimensao/posicao de onde voce veio (e vice-versa).
- A dimensao nao tem nenhum spawn de mobs/entidades (bioma sem spawners, sem estruturas, sem features).

## Compilar
Suba esta pasta para um repositorio no GitHub: a Action `.github/workflows/build.yml`
compila e disponibiliza o `.jar` em *Actions > build > Artifacts*.
Localmente: `gradle build` (Gradle 8.5 + JDK 17); o jar fica em `build/libs/`.

## Ajustes visuais
- Brilho geral: `ambient_light` em `data/studio/dimension_type/studio.json` (0.0 a 1.0).
- Cor da neblina/fundo: `fog_color` em `data/studio/worldgen/biome/throne_void.json` (valor decimal da cor final, 1709604 = #1A1624).
