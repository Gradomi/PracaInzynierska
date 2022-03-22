package pl.moja.biblioteczka.database.dao;

import pl.moja.biblioteczka.database.dbuitls.DbManager;
import pl.moja.biblioteczka.models.Composition;
import pl.moja.biblioteczka.models.Material;
import pl.moja.biblioteczka.models.MaterialComposition;
import pl.moja.biblioteczka.models.SearchParameter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MaterialDao implements AbstractDao<Material> {
    private final DbManager dbManager;
    private final MaterialCompositionDao materialCompositionDao;
    private final CompositionDao compositionDao;

    public MaterialDao(DbManager dbManager) {
        this.dbManager = dbManager;
        this.compositionDao = new CompositionDao(this.dbManager);
        this.materialCompositionDao = new MaterialCompositionDao(this.dbManager);
    }

    @Override
    public List<Material> get() throws SQLException {
        List<Material> materials = new ArrayList<>();
        ResultSet resultSet;
        try (Statement statement = dbManager.getConnection().createStatement()) {
            if (statement.execute("SELECT * FROM material ORDER BY material_id")) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    int id = resultSet.getInt("material_id");
                    materials.add(buildMaterialFrom(resultSet, getCompositionString(id)));
                }
            }
            return materials;
        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return materials;

    }

    @Override
    public Material getById(int id) {
        ResultSet resultSet;
        try (Statement statement = dbManager.getConnection().createStatement()) {
            if (statement.execute("SELECT * FROM material WHERE material_id = " + id)) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    return buildMaterialFrom(resultSet, getCompositionString(id));
                }
            }
        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return null;
    }

    @Override
    public List<Material> getByIds(List<Integer> ids) throws SQLException {
        ResultSet resultSet;
        List<Material> materials = new ArrayList<>();
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = prepareQueryString(ids);
            if (statement.execute(query)) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    materials.add(buildMaterialFrom(resultSet));
                }
            }

            return materials;
        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return materials;
    }

    public List<Material> getByParameters(List<SearchParameter> parameters) {
        ResultSet resultSet;
        List<Material> materials = new ArrayList<>();
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = prepareSelectQueryString(parameters);
            if (statement.execute(query)) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    materials.add(buildMaterialFrom(resultSet));
                }
            }

            return materials;
        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return materials;
    }

    @Override
    public int save(Material material) throws SQLException {
        ResultSet resultSet;
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = prepareInsertQuery(List.of(material));
            if(statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS) != 0) {
                resultSet = statement.getGeneratedKeys();
                if(resultSet != null && resultSet.next()){
                return resultSet.getInt(1);
                }
            }

        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return 0;
    }

    @Override
    public List<Integer> save(List<Material> listT) throws SQLException {
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
    public Material delete(Material material) throws SQLException {
        ResultSet resultSet;
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = "DELETE FROM material WHERE material_id = '" + material.getId() + "');";
            if (statement.execute(query)) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    return buildMaterialFrom(resultSet);
                }
            }

        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return null;
    }

    @Override
    public List<Material> delete(List<Material> listT) throws SQLException {
        ResultSet resultSet;
        List<Material> materials = new ArrayList<>();
        try (Statement statement = dbManager.getConnection().createStatement()) {
            String query = prepareInsertQuery(listT);
            if (statement.execute(query)) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    materials.add(buildMaterialFrom(resultSet));
                }
                return materials;
            }
        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return materials;
    }


    @Override
    public Material update(Material material) throws SQLException {
        ResultSet resultSet;
        try (Statement statement = dbManager.getConnection().createStatement()) {

            String query = prepareUpdateQuery(material);
            if (statement.execute(query)) {
                resultSet = statement.getResultSet();
                while (resultSet != null && resultSet.next()) {
                    return buildMaterialFrom(resultSet);
                }
            }

        } catch (SQLException sqlException) {
            DbManager.printSQLException(sqlException);
        }
        return null;
    }

    @Override
    public List<Material> update(List<Material> listT) throws SQLException {
        List<Material> materials = new ArrayList<>();
        listT.forEach(material -> {
            try (Statement statement = dbManager.getConnection().createStatement()) {
                String query = prepareUpdateQuery(material);
                if (statement.execute(query)) {
                    final ResultSet resultSet = statement.getResultSet();
                    while (resultSet != null && resultSet.next()) {
                        materials.add(buildMaterialFrom(resultSet));
                    }
                }
            } catch (SQLException sqlException) {
                DbManager.printSQLException(sqlException);
            }
        });

        return materials;

    }

    private String prepareQueryString(List<Integer> ids) {
        return String.format("SELECT * FROM material WHERE material_id IN (%s);",
                ids.stream().map(Object::toString)
                        .collect(Collectors.joining(",")));
    }

    private String prepareSelectQueryString(List<SearchParameter> parameters) {
        StringBuilder str = new StringBuilder();
        List<SearchParameter> params = parameters.stream().filter(param -> param.getValue() != null || !param.getValue().isEmpty()).toList();

        str.append("SELECT * FROM material WHERE ");
        for (int i = 0; i < params.size(); i++) {
            SearchParameter var = params.get(i);
            if (i == params.size() - 1) {
                str.append(var.getColumnName()).append("= '").append(var.getValue()).append("' ;");
            } else {
                str.append(var.getColumnName()).append("= '").append(var.getValue()).append("' AND ");

            }
        }
        return str.toString().toLowerCase();
    }

    private List<Composition> getMaterialCompositions(int id) throws SQLException {
        List<MaterialComposition> materialCompositionList = materialCompositionDao.get();
        return compositionDao.getByIds(materialCompositionList.stream()
                .filter(composition -> composition.getMaterialId() == id)
                .map(MaterialComposition::getChemicalCompositionId)
                .toList());
    }


    private Material buildMaterialFrom(ResultSet resultSet) throws SQLException {
        return Material.builder()
                .id(resultSet.getInt("material_id"))
                .materialName(resultSet.getString("material_name"))
                .country(resultSet.getString("country"))
                .norm(resultSet.getString("norm"))
                .yieldStrenghtMin(resultSet.getBigDecimal("yield_strength_min"))
                .yieldStrenghtMax(resultSet.getBigDecimal("yield_strength_max"))
                .percentageElongationMin(resultSet.getBigDecimal("percentage_elongation_min"))
                .percentageElongationMax(resultSet.getBigDecimal("percentage_elongation_max"))
                .brinellHardnessMin(resultSet.getBigDecimal("brinell_hardness_min"))
                .brinellHardnessMax(resultSet.getBigDecimal("brinell_hardness_max"))
                .impactStrengthMin(resultSet.getBigDecimal("impact_strength_min"))
                .impactStrengthMax(resultSet.getBigDecimal("impact_strength_max"))
                .tensileStrengthMin(resultSet.getBigDecimal("tensile_strength_min"))
                .tensileStrengthMax(resultSet.getBigDecimal("tensile_strength_max"))
                .favourite(resultSet.getBoolean("favourite"))
                .build();
    }


    private Material buildMaterialFrom(ResultSet resultSet, String compositions) throws SQLException {
        Material material = buildMaterialFrom(resultSet);
        material.setMaterialComposition(compositions);
        return material;
    }


    private String prepareInsertQuery(List<Material> materials) {
        return String.format("INSERT INTO material (" +
                                "material_id," +
                                "material_name, " +
                                "country, " +
                                "norm, " +
                                "yield_strength_min, " +
                                "yield_strength_max, " +
                                "percentage_elongation_min, " +
                                "percentage_elongation_max, " +
                                "brinell_hardness_min, " +
                                "brinell_hardness_max, " +
                                "tensile_strength_min, " +
                                "tensile_strength_max, " +
                                "impact_strength_min, " +
                                "impact_strength_max " +
                                ") VALUES %s;",
                        materials.stream().map(material -> "(0,'"
                                        + material.getMaterialName() + "', '"
                                        + material.getCountry() + "', '"
                                        + material.getNorm() + "', '"
                                        + material.getYieldStrenghtMin() + "', '"
                                        + material.getYieldStrenghtMax() + "', '"
                                        + material.getPercentageElongationMin() + "', '"
                                        + material.getPercentageElongationMax() + "', '"
                                        + material.getBrinellHardnessMin() + "', '"
                                        + material.getBrinellHardnessMax() + "', '"
                                        + material.getTensileStrengthMin() + "', '"
                                        + material.getTensileStrengthMax() + "', '"
                                        + material.getImpactStrengthMin() + "', '"
                                        + material.getImpactStrengthMax() + "'),")
                                .collect(Collectors.joining("\n\r")))
                .replace("),", ")");
    }

    private String prepareUpdateQuery(Material material) {
        return "UPDATE material SET " +
                                "material_name='" + material.getMaterialName() + "'," +
                                "country='" + material.getCountry() + "'," +
                                "norm='" + material.getNorm() + "'," +
                                "yield_strength_min='" + material.getYieldStrenghtMin() + "'," +
                                "yield_strength_max='" + material.getYieldStrenghtMax()+ "'," +
                                "percentage_elongation_min='" + material.getPercentageElongationMin() + "'," +
                                "percentage_elongation_max='" + material.getPercentageElongationMax()+ "'," +
                                "brinell_hardness_min='" + material.getBrinellHardnessMin() + "'," +
                                "brinell_hardness_max='" + material.getBrinellHardnessMax() + "'," +
                                "tensile_strength_min=" + material.getTensileStrengthMin() + "," +
                                "tensile_strength_max=" + material.getTensileStrengthMax() + "," +
                                "impact_strength_min='" + material.getImpactStrengthMin() + "'," +
                                "impact_strength_max='" + material.getImpactStrengthMax() + "', " +
                                "favourite='" + material.isFavouriteBinary() + "' " +
                                " WHERE material_id = " + material.getId();
    }


    private String getCompositionString(int id) throws SQLException {
        List<MaterialComposition> materialCompositions = materialCompositionDao.getByIds(List.of(id));
        List<Composition> compositions = compositionDao.getByIds(materialCompositions.stream().map(MaterialComposition::getChemicalCompositionId).collect(Collectors.toList()));
        compositions.forEach(composition -> composition.setMaterialComposition(materialCompositions.stream()
                .filter(mComposition -> mComposition.getChemicalCompositionId() == composition.getSymbolId())
                .findFirst()
                .orElse(null)));
        return compositions.stream().map(Composition::getCompositionString).collect(Collectors.joining(",     "));
    }
}



