package db.migration;

import java.lang.Exception;
import java.lang.Override;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class V1_0_0_20210427043928__this_is_a_test extends BaseJavaMigration {
  private static final Logger LOG = LoggerFactory.getLogger(V1_0_0_20210427043928__this_is_a_test.class);

  @Override
  public void migrate(Context context) throws Exception {
    // Implement the migration here
  }
}
