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

import org.multiverse.api.StmUtils;
import org.multiverse.api.Txn;
import org.multiverse.api.callables.TxnDoubleCallable;
import org.multiverse.api.collections.TxnList;

public class StmGroup {

  private final TxnList<StmMember> members = StmUtils.newTxnLinkedList();

  public void add(final StmMember member) {
    StmUtils.atomic(new Runnable() {
      @Override
      public void run() {
        members.add(member);
        member.setGroup(StmGroup.this);
      }
    });
  }

  public double calculateAverage() {

    return StmUtils.atomic(new TxnDoubleCallable() {
      @Override
      public double call(final Txn txn) throws Exception {
        if (members.isEmpty()) {
          return 0;
        }

        double total = 0;

        for (int i = 0, size = members.size(); i < size; i++) {
          total += members.get(txn, i).getScore();
        }

        return total / members.size();
      }
    });

  }

}
