//package mh.example.auth;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.beans.factory.support.DefaultListableBeanFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SessionFixBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
//    private static final String SERIALIZATION_ID = "4086d293-966c-4d89-8485-f1c1f5c092456";
//    public SessionFixBeanFactoryPostProcessor() {
//		// TODO Auto-generated constructor stub
//    	
//    	
//	}
//
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        if ((beanFactory instanceof DefaultListableBeanFactory)) {
//            DefaultListableBeanFactory dlbf = (DefaultListableBeanFactory) beanFactory;
//            dlbf.setSerializationId(SERIALIZATION_ID);
//        }
//    }
//}
