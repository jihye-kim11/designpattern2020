# designpattern2020    
HTML exporter:주어진 데이터를 이용하여 HTML 파일을 생성하는 역할을 한다. 기본형식은 csv exporter와 유사하게 작성하였으며 내부 구성요소들을 형식에 알맞게 수정해주었다. 그에 따라 생성된 파일은 people.html로 DP2020Project/ 하단에서 확인할 수 있다.     
xml exporter: 주어진 데이터를 이용하여 xml 파일을 생성하는 역할을 한다. 기본형식은 csv exporter와 유사하게 작성하였으며 내부 구성요소들을 형식에 알맞게 수정해주었다. 그에 따라 생성된 파일은 people.xml로 DP2020Project/ 하단에서 확인할 수 있다.    
xml importer: exporter와는 반대로 파일을 읽어오는 역할을 한다. people.xml을 읽어와 콘솔창에 테이블을 나태내 보여주는 역할을 한다.    
concreteTable.java: select * from address, name where address.addrId = name.addrId 오류 수정을 위해 요청받은 컬럼들을 읽어오는 부분인 concreteTable.java의 public Table select(Selector where, Collection requestedColumns, Collection other)을 수정하였다. console을 빌드하여 오류 수정을 확인할 수 있다.    
Visitor.java: ViewVisitor 클래스는 데이터 구조를 방문하면서 만들어진 파일 위치를 출력하는 역할을 한다.     
ViewVisitor.java: ViewVisitor 클래스는 데이터 구조를 방문하면서 만들어진 파일 위치를 출력하는 역할을 한다. XmlImporter.java의 test class 실제 사용 과정을 확인할 수 있다.
