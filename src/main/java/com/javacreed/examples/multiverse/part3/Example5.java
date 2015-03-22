/*
 * #%L
 * Address Deadlocks using Multiverse
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2012 - 2015 Java Creed
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.javacreed.examples.multiverse.part3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Example5 {

  public static final Logger LOGGER = LoggerFactory.getLogger(Example5.class);

  public static void main(final String[] args) throws InterruptedException {
    final StmGroup group = new StmGroup();
    final StmMember member = new StmMember();
    group.add(member);

    final Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        group.calculateAverage();
      }
    }, "T1");

    final Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        member.isAboveAverage();
      }
    }, "T2");

    // Start both threads
    Example5.LOGGER.debug("Start both thread");
    t1.start();
    t2.start();

    // Wait for both threads to finish
    Example5.LOGGER.debug("Wait for both threads to finish");
    t1.join();
    t2.join();

    Example5.LOGGER.debug("Both threads finished");
    Example5.LOGGER.debug("Done.");
  }

}
