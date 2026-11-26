@echo off

REM REM serve para fazer comentários
REM @echo off faz não mostar os comandos chamados no terminal, apenas suas saídas
REM devemos chamar call, pois o gradle chama outros arquivos .bat
REM sem o call, esse .bat ia encerrar e dar vez ao .bat do gradle
REM com o call ele chama os .bat do gradle e espera terminar pra continuar

REM call gradle clean build --refresh-dependencies

call gradle clean

cls

call gradle build

cls

call gradle run