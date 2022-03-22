package pl.moja.biblioteczka.database.dao;

import lombok.RequiredArgsConstructor;
import pl.moja.biblioteczka.database.dbuitls.DbManager;
import pl.moja.biblioteczka.models.Composition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CompositionDao implements AbstractDao<Composition> {

    private final DbManager dbManager;

    @Override
    public List<Composition> get() {
        ResultSet resultSet;
        List<Composition> resultComposition = new ArrayList<>();

        try (Statement statement = dbManager.getConnection().createStatement()) {
            if (statement.execute("SELECT * FROM chemical_composition")) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    resultComposition.add(getFrom(resultSet));
                }
            }
            return resultComposition;
        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return resultComposition;
    }

    @Override
    public Composition getById(int id) throws SQLException {
        ResultSet resultSet;
        try (Statement statement = dbManager.getConnection().createStatement()) {

            if (statement.execute("SELECT * FROM chemical_composition WHERE chemical_composition_id = " + id)) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    return getFrom(resultSet);
                }
            }
        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return null;

    }

    @Override
    public List<Composition> getByIds(List<Integer> ids) throws SQLException {
        ResultSet resultSet;
        List<Composition> resultComposition = new ArrayList<>();
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = prepareQueryString(ids);
            if (statement.execute(query)) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    resultComposition.add(getFrom(resultSet));
                }
            }

            return resultComposition;
        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return resultComposition;

    }

    @Override
    public int save(Composition composition) {
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = "INSERT INTO chemical_composition ('chemical_composition_id', 'symbol') VALUES (0, '" + composition.getSymbolName() + "');";
            return statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return 0;
    }

    @Override
    public List<Integer> save(List<Composition> listT) throws SQLException {

        ResultSet resultSet;
        List<Integer> ids = new ArrayList<>();
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = prepareInsertQuery(listT);
            if (statement.execute(query)) {
                resultSet = statement.getGeneratedKeys();
                while (resultSet != null && resultSet.next()) {
                    ids.add(resultSet.getInt(1));
                }
            }
            return ids;

        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return ids;
    }

    private String prepareInsertQuery(List<Composition> compositions) {
        return String.format("INSERT INTO chemical_composition ('chemical_composition_id', 'symbol') VALUES (%n%s);",
                        compositions.stream().map(composition -> "(0,'" + composition.getSymbolName() + "'),")
                                .collect(Collectors.joining("\n\r")))
                .replace(",)", ")");
    }

    private Composition getFrom(ResultSet resultSet) throws SQLException {
        return Composition.builder()
                .symbolId(resultSet.getInt("chemical_composition_id"))
                .symbolName(resultSet.getString("symbol"))
                .build();
    }

    private String prepareQueryString(List<Integer> ids) {
        return String.format("SELECT * FROM chemical_composition WHERE chemical_composition_id IN (%s);",
                ids.stream().map(Object::toString)
                        .collect(Collectors.joining(",")));
    }

    @Override
    public Composition delete(Composition composition) {
        ResultSet resultSet;
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = "DELETE FROM chemical_composition WHERE chemical_composition_id = '" + composition.getSymbolId() + "'); ";
            if (statement.execute(query)) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    return getFrom(resultSet);
                }
            }

        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return null;
    }

    @Override
    public List<Composition> delete(List<Composition> listT) throws SQLException {
        ResultSet resultSet;
        List<Composition> compositions = new ArrayList<>();
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = prepareInsertQuery(listT);
            if (statement.execute(query)) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    compositions.add(getFrom(resultSet));
                }
                return compositions;
            }
        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return compositions;
    }


    @Override
    public Composition update(Composition composition) throws SQLException {
        ResultSet resultSet;
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = "UPDATE chemical_composition SET symbol = '" + composition.getSymbolName() + "' WHERE chemical_composition_id = '" + composition.getSymbolId() + "'; ";
            if (statement.execute(query)) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    return getFrom(resultSet);
                }
            }

        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return null;
    }


    @Override
    public List<Composition> update(List<Composition> listT) throws SQLException {
        ResultSet resultSet;
        List<Composition> compositions = new ArrayList<>();
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = prepareInsertQuery(listT);
            if (statement.execute(query)) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    compositions.add(getFrom(resultSet));
                }
                return compositions;
            }
        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return compositions;

    }


}


