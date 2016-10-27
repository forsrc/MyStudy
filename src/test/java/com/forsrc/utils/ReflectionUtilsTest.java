package com.forsrc.utils;


import java.text.MessageFormat;

import net.sf.cglib.beans.BeanCopier;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;


public class ReflectionUtilsTest {

	public static void main(String[] args) throws Exception {
		A a = new A();
		a.setAge(1);
		a.setName("A");
		A b = new A();

		test(new Handle() {
			@Override
			public void loop(A a, A b) throws Exception {
			}
		});
		test(new Handle() {
			@Override
			public void loop(A a, A b) throws Exception {
			}
		});
		System.out.println("--------");
		System.out.println("BeanUtils.copyProperties(a, b);");
		test(new Handle() {
			@Override
			public void loop(A a, A b) throws Exception {
				BeanUtils.copyProperties(a, b);
			}
		});
		System.out.println("PropertyUtils.copyProperties(a, b);");
		test(new Handle() {
			@Override
			public void loop(A a, A b) throws Exception {
				PropertyUtils.copyProperties(a, b);
			}
		});
		System.out.println("org.springframework.beans.BeanUtils.copyProperties(a, b);");
		test(new Handle() {
			@Override
			public void loop(A a, A b) throws Exception {
				org.springframework.beans.BeanUtils.copyProperties(a, b);
			}
		});
		System.out.println("beanCopier.copy(b, a, null);");
		final BeanCopier beanCopier = BeanCopier.create(A.class, A.class, false);
		test(new Handle() {
			@Override
			public void loop(A a, A b) throws Exception {
				beanCopier.copy(b, a, null);
			}
		});
		/*System.out.println("MyBeanUtils.copyProperties(A.class, a, b, false);");
		test(new Handle() {
			@Override
			public void loop(A a, A b) throws Exception {
				MyBeanUtils.copyProperties(A.class, b, a, true);
			}
		});*/
		System.out.println("--------");

	}

	static final void test(Handle handle) throws Exception {
		A a = new A();
		a.setAge(1);
		a.setName("A");
		A b = new A();
		
		long start = System.nanoTime();
		for (int i = 0; i < 1000; i++) {
			handle.loop(a, b);
		}
		long end = System.nanoTime() - start;
		System.out.println(MessageFormat.format("{0}", end));
	}

	static interface Handle {
		//public static final BeanCopier beanCopier = BeanCopier.create(A.class, A.class, false);
		void loop(A a, A b) throws Exception;
	}


}

class A {
	private String name;
	private int age;
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getAge() {
		return this.age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
}