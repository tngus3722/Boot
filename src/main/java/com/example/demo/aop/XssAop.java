package com.example.demo.aop;

import com.example.demo.annotation.XSS;
import com.example.demo.annotation.XssExclude;
import com.nhncorp.lucy.security.xss.LucyXssFilter;
import com.nhncorp.lucy.security.xss.XssSaxFilter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 김통일에 의해 개발되었으며, 자유롭게 사용, 수정이 가능하다.
 * xhddlf8070@gmail.com
 * 해당 VaildXss는 String.class Type만 타켓팅하여 필터링한다.
 *
 * Class : org.tikim.boot.domain.test.Test
 * Class : org.tikim.boot.domain.test.Test2
 * 위 2개의 Class를 예시로 사용하고 있다.
 *
 * 결과 사진 : https://static-sample.tikim.org/2020/07/20/28659dc7-634c-46b7-9678-09a7e2c36d86-1595235709288.PNG
 * 사진의 왼쪽이 before
 * 사진의 오른쪽이 after
 *
 *   사용가능한 형태
 *
 * 	    requestBody의 Type이 String인 경우
 *      requestBody의 Type에 @Xss annotation이 붙어 있는 경우
 *          - @Xss가 선언되고 있는 Type 내부의 Field Type이 String인 경우
 *              * 해당 Field에 @XssExclude가 붙어있는 경우 필터링에서 제외된다.
 *
 *          - @Xss가 선언되고 있는 Type 내부의 Field Type이 @Xss annotation이 붙어있는 Type인 경우
 *              * private Test2 test2;   -> Test2 Type 상단에 @Xss Annotation이 붙어있는 경우
 *
 *          - @Xss가 선언되고 있는 Type 내부의 Field Type이 Collection<? has annotation @Xss>인 경우
 *              * private List<Test2> test2List;
 *
 *          - @Xss가 선언되고 있는 Type 내부의 Field Type이 Object[]인 경우. 단 Object 대상은 @Xss가 선언되어 있어야한다.
 *              * private Test2[] test2Arrays;
 *
 *   예외 사항
 *
 * List<String> testStringList;
 *          - @Xss가 선언되고 있는 Type 내부의 Field Type이 Collection<String>인 경우
 * 	            * private List<String> testStringList;
 *
 *          - @Xss가 선언되고 있는 Type 내부의 Field Type이 String[]인 경우
 * 	            * private String[] testStringArrays;
 */
@Aspect
@Component
public class XssAop {

    private LucyXssFilter filter = XssSaxFilter.getInstance("lucy-xss-sax.xml",true);

    @Around(value = "@annotation(com.example.demo.annotation.XSS)")
    public Object validxss(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature(); // Method signature 가져옴
        Annotation[][] annotationMatrix = methodSignature.getMethod().getParameterAnnotations();  // parameter에 붙은 annotation을 가져옴 반환형은 2차원배열
        int index = -1;
        for(Annotation[] annotations : annotationMatrix){
            index++;
            for(Annotation annotation : annotations){
                if(!(annotation instanceof RequestBody)){ // 출력 ex @org.springframework.web.bind.annotation.RequestBody(required=true)
                    continue; // requestbody 가 아니면 다음 반복문 실행
                }
             //   System.out.println("arg " + index + " " + proceedingJoinPoint.getArgs()[index]); // ex -> arg 0 com.example.demo.domain.Review@382dc5dd
                proceedingJoinPoint.getArgs()[index] = xssCheck(proceedingJoinPoint.getArgs()[index]); // arg[n]을 xss(arg[n])로 치환
                return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());// 끝
            }
        }
        return proceedingJoinPoint.proceed(); // requestbody가 없는 경우 끝
    }
    private Object xssCheck(Object vo) throws Exception {
        if(vo==null) return vo; // arg를 받아서
        if(vo.getClass() == String.class) return filter.doFilter((String) vo); // String이면 바로 pure String 반환

        BeanInfo info = Introspector.getBeanInfo(vo.getClass(),Object.class); // bean의 정보를 받아옴
       // System.out.println("vo getinfo = " +vo.getClass()); //ex class com.example.demo.domain.Review
       // System.out.println("bean info = " + info.toString()); // ex java.beans.GenericBeanInfo@252840e7

        for (PropertyDescriptor pd : info.getPropertyDescriptors()) { // 반복문, getter를 가져옴
            Method reader = pd.getReadMethod(); // one of the getters
            if (reader != null ) { // getter가 있다면
            //    System.out.println("Read" + reader); // ex Readpublic java.lang.String com.example.demo.domain.Review.getContent()
                Object object = reader.invoke(vo); // 해당 getter 실행
           //     System.out.println(object); // ex titletest, <script>, null, 1, ... 객체의 field들..
        //        if (object != null)
         //           System.out.println(object.getClass().isAnnotationPresent(XSS.class)); //  모두 false일 것임 xss를 붙인 review field에 객체가 없음( 애초에 객체도없음 )
                if (object != null && object.getClass().isAnnotationPresent(XSS.class)) { //객체에 class가 xss annotaion이 부여되있다면
                    xssCheck(object); // xss check 재귀
                    continue;
                }

                if (object != null && object instanceof Collection) { // getter의 결과가 collection이라면?
                    for (Object o : ((Collection<Object>) object)) { // collection의 내용들을
                        xssCheck(o); // xss filter처리함
                    }
                }
                if (object != null && object.getClass().isArray()) { // 배열이라면
                    for (Object o : ((Object[]) object)) { //  인덱스 전부 필터 처리
                        xssCheck(o);
                    }
                }

                if (object != null && object.getClass() == String.class) { // 문자열이면
                    if (vo.getClass().getDeclaredField(getFieldName(reader)).isAnnotationPresent(XssExclude.class)) { // 객채의 필드가
                        //Field getDeclaredField(String name): name과 동일한 이름으로 정의된 변수를 Field 클래스 타입으로 리턴한다.
                        //https://12bme.tistory.com/129
                        continue;
                    }
                    Method setter = vo.getClass().getMethod(getSetterMethodString(reader), String.class); // setter를 가져옴
                    setter.invoke(vo, filter.doFilter(object.toString())); // xss filter 해서 다시 set
                    continue;
                }

            }
        }
        return vo;
    }
    private String getSetterMethodString(Method getterMethod){
        String getterMethodName = getterMethod.getName();
        if(!getterMethodName.startsWith("get")){
           System.out.println("error get setter method String");
        }
        String setterMethodName = getterMethodName.replaceFirst("get","set");
        return setterMethodName;
    }

    private String getFieldName(Method method){
        String methodName = method.getName();
        if(methodName.startsWith("get")||method.getName().startsWith("set")){
            return methodName.substring(3,4).toLowerCase()+ methodName.substring(4,methodName.length()); // Ex getInfo -> info 추출
        }
       System.out.println("error get field name");
        return "error";
    }
}
