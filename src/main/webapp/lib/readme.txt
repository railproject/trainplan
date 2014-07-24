如下命令是将lib下的jar加载到本地maven数据仓库，Dfile是jar的路径，需要根据实际路径更改

mvn install:install-file -Dfile=C:\铁路集中计划编制-客车开行计划子平台\mor.railway.cmd.adapter.jar -DgroupId=mor.railway.cmd.adapter -DartifactId=mor.railway.cmd.adapter -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=C:\铁路集中计划编制-客车开行计划子平台\itcmor.util.jar -DgroupId=itcmor.util -DartifactId=itcmor.util -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=C:\铁路集中计划编制-客车开行计划子平台\jdom.jar -DgroupId=itcmor.util -DartifactId=jdom -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=C:\铁路集中计划编制-客车开行计划子平台\TableEditor.jar -DgroupId=TableEditor -DartifactId=TableEditor -Dversion=1.0 -Dpackaging=jar


