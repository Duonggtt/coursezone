mvn -pl . clean install & ^
mvn -pl coursezone-sdk clean install & ^
mvn -pl coursezone-admin-plugin clean install -Pexport,\!test & ^
mvn -pl coursezone-web-plugin clean install -Pexport,\!test & ^
mvn -pl coursezone-theme clean install -Pexport,\!test & ^
ezy.bat package & ^
ezy.bat export
