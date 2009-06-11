package liquibase.precondition.core;

import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.structure.DatabaseSnapshot;
import liquibase.exception.JDBCException;
import liquibase.exception.PreconditionErrorException;
import liquibase.exception.PreconditionFailedException;
import liquibase.util.StringUtils;
import liquibase.precondition.Precondition;

public class ViewExistsPrecondition implements Precondition {
    private String schemaName;
    private String viewName;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = StringUtils.trimToNull(schemaName);
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public void check(Database database, DatabaseChangeLog changeLog) throws PreconditionFailedException, PreconditionErrorException {
        DatabaseSnapshot databaseSnapshot;
        try {
            databaseSnapshot = database.createDatabaseSnapshot(getSchemaName(), null);
        } catch (JDBCException e) {
            throw new PreconditionErrorException(e, changeLog, this);
        }
        if (databaseSnapshot.getView(getViewName()) == null) {
            throw new PreconditionFailedException("View "+database.escapeStringForDatabase(getViewName())+" does not exist", changeLog, this);
        }
    }

    public String getName() {
        return "viewExists";
    }
}