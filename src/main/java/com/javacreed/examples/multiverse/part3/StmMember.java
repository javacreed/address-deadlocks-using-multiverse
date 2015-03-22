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
import org.multiverse.api.callables.TxnBooleanCallable;
import org.multiverse.api.callables.TxnDoubleCallable;
import org.multiverse.api.references.TxnDouble;
import org.multiverse.api.references.TxnRef;

public class StmMember {

  private final TxnRef<StmGroup> group = StmUtils.newTxnRef();
  private final TxnDouble score = StmUtils.newTxnDouble();

  public double getScore() {
    return StmUtils.atomic(new TxnDoubleCallable() {
      @Override
      public double call(final Txn txn) throws Exception {
        return score.get(txn);
      }
    });
  }

  public boolean isAboveAverage() {
    return StmUtils.atomic(new TxnBooleanCallable() {
      @Override
      public boolean call(final Txn txn) throws Exception {
        return score.get(txn) > group.get(txn).calculateAverage();
      }
    });
  }

  public void setGroup(final StmGroup group) {
    StmUtils.atomic(new Runnable() {
      @Override
      public void run() {
        StmMember.this.group.set(group);
      }
    });
  }

  public void setScore(final double score) {
    StmUtils.atomic(new Runnable() {
      @Override
      public void run() {
        StmMember.this.score.set(score);
      }
    });
  }

}
