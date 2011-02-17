/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: Bootstrapper.java 70 2010-06-23 07:03:56Z ryan $
 */
package au.org.intersect.entreeui.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @version $Rev: 70 $
 * 
 */
public class Bootstrapper
{

    public static void main(String[] args)
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        Controller controller = (Controller) context.getBean("controller");
        controller.start();
    }
}
