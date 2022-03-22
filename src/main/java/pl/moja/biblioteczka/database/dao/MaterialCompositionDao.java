package pl.moja.biblioteczka.database.dao;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.moja.biblioteczka.database.dbuitls.DbManager;
import pl.moja.biblioteczka.models.MaterialComposition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MaterialCompositionDao implements AbstractDao<MaterialComposition> {
    private final DbManager dbManager;

    @Override
    public List<MaterialComposition> get() throws SQLException {
        ResultSet resultSet;
        List<MaterialComposition> resultComposition = new ArrayList<>();
        try (Statement statement = dbManager.getConnection().createStatement()) {
            if (statement.execute("SELECT * FROM material_composition")) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    resultComposition.add(MaterialComposition.builder()
                            .materialId(resultSet.getInt("material_id"))
                            .chemicalCompositionId(resultSet.getInt("chemical_composition_id"))
                            .compositionRatioMin(resultSet.getFloat("composition_ratio_min"))
                            .compositionRatioMax(resultSet.getFloat("composition_ratio_max"))
                            .build());
                }
            }
            return resultComposition;
        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return resultComposition;
    }

    @Override
    public MaterialComposition getById(int id) throws SQLException {
        ResultSet resultSet;
        try (Statement statement = dbManager.getConnection().createStatement()) {

            if (statement.execute("SELECT * FROM material_composition WHERE material_id = " + id)) {
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
    public List<MaterialComposition> getByIds(List<Integer> ids) throws SQLException {
        ResultSet resultSet;
        List<MaterialComposition> resultMaterialComposition = new ArrayList<>();
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = prepareQueryString(ids);
            if (statement.execute(query)) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    resultMaterialComposition.add(getFrom(resultSet));
                }
            }

            return resultMaterialComposition;
        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return resultMaterialComposition;


    }

    @Override
    public int save(MaterialComposition materialComposition) throws SQLException {
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = "INSERT INTO material_composition ('material_id', 'chemical_composition_id', 'composition_ratio_min', 'composition_ratio_max') VALUES ('"
                    + materialComposition.getMaterialId() + "', '" +
                    +materialComposition.getChemicalCompositionId() + "', '" +
                    +materialComposition.getCompositionRatioMin()
                    + "', '" + materialComposition.getCompositionRatioMax() + "' );";
            return statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return 0;
    }

    @Override
    public List<Integer> save(@NonNull List<MaterialComposition> listT) throws SQLException {
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

    @Override
    public MaterialComposition delete(MaterialComposition materialComposition) throws SQLException {
        ResultSet resultSet;
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = "DELETE FROM material_composition WHERE material_id = '" + materialComposition.getMaterialId() + "'); ";
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
    public List<MaterialComposition> delete(List<MaterialComposition> listT) throws SQLException {
        ResultSet resultSet;
        List<MaterialComposition> materialCompositions = new ArrayList<>();
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = prepareInsertQuery(listT);
            if (statement.execute(query)) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    materialCompositions.add(getFrom(resultSet));
                }
                return materialCompositions;
            }
        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return materialCompositions;
    }

    @Override
    public MaterialComposition update(MaterialComposition materialComposition) throws SQLException {
        ResultSet resultSet;
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = "UPDATE material_composition SET composition_ratio_min, composition_ratio_max = '" + materialComposition.getCompositionRatioMin() + " , '" +
                    +materialComposition.getCompositionRatioMax() + "' WHERE material_id = '" + materialComposition.getCompositionRatioMax() + "';";
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
    public List<MaterialComposition> update(List<MaterialComposition> listT) throws SQLException {
        ResultSet resultSet;
        List<MaterialComposition> materialCompositions = new ArrayList<>();
        try (Statement statement = dbManager.getConnection().createStatement()) {

            String query = prepareInsertQuery(listT);
            if (statement.execute(query)) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    materialCompositions.add(getFrom(resultSet));
                }
                return materialCompositions;
            }
        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return materialCompositions;

    }

    private MaterialComposition getFrom(ResultSet resultSet) throws SQLException {
        return MaterialComposition.builder()
                .materialId(resultSet.getInt("material_id"))
                .chemicalCompositionId(resultSet.getInt("chemical_composition_id"))
                .compositionRatioMin(resultSet.getFloat("composition_ratio_min"))
                .compositionRatioMax(resultSet.getFloat("composition_ratio_max"))
                .build();
    }

    private static String prepareInsertQuery(List<MaterialComposition> materialCompositions) {
        if (materialCompositions.size() == 1) {
            return String.format("INSERT INTO material_composition (material_id, chemical_composition_id, composition_ratio_min, composition_ratio_max) VALUES (%s);",
                    materialCompositions.stream().findFirst().map(materialComposition ->
                            materialComposition.getMaterialId() + ","
                                    + materialComposition.getChemicalCompositionId() + ","
                                    + materialComposition.getCompositionRatioMin() + ","
                                    + materialComposition.getCompositionRatioMax())
                            .orElse(null));

        }
        return String.format("INSERT INTO material_composition (material_id, chemical_composition_id, composition_ratio_min, composition_ratio_max) VALUES %s;",
                        materialCompositions.stream().map(materialComposition ->
                                       "(" + materialComposition.getMaterialId() + ","
                                               + materialComposition.getChemicalCompositionId() + ","
                                                + materialComposition.getCompositionRatioMin() + ","
                                                + materialComposition.getCompositionRatioMax() + "),")
                                .collect(Collectors.joining(",")))
                .replace("),", ")");
    }

    private String prepareQueryString(List<Integer> ids) {
        if (ids.size() == 1) {
            return String.format("SELECT * FROM material_composition WHERE material_id = %s;", ids.get(0));
        }
        return String.format("SELECT * FROM material_composition WHERE material_id IN (%s);",
                ids.stream().map(Object::toString)
                        .collect(Collectors.joining(",")));
    }

}
