package com.gmail.chickenpowerrr.ranksync.discord;

import com.gmail.chickenpowerrr.ranksync.api.*;
import com.gmail.chickenpowerrr.ranksync.api.Rank;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

class SqlDatabase implements Database {

    private final HikariDataSource dataSource;
    private final RankResource rankResource;
    private final Bot<?,?> bot;

    SqlDatabase(Bot bot, Properties properties) {
        this.bot = bot;

        if(properties.has("max_pool_size", "host", "port", "database", "username", "password")) {
            this.dataSource = new HikariDataSource();

            this.dataSource.setMaximumPoolSize(properties.getInt("max_pool_size"));
            this.dataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            this.dataSource.addDataSourceProperty("serverName", properties.getString("host"));
            this.dataSource.addDataSourceProperty("port", properties.getInt("port"));
            this.dataSource.addDataSourceProperty("databaseName", properties.getString("database"));
            this.dataSource.addDataSourceProperty("user", properties.getString("username"));
            this.dataSource.addDataSourceProperty("password", properties.getString("password"));
        } else {
            throw new IllegalStateException("Not all of the required properties for an SQL database have been entered");
        }

        if(properties.has("rank_resource")) {
            this.rankResource = (RankResource) properties.getObject("rank_resource");
        } else {
            throw new IllegalStateException("This resource needs a rank resource");
        }

        try(Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT IGNORE bot (name) VALUES (?);")) {
            preparedStatement.setString(1, this.bot.getName());
            preparedStatement.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CompletableFuture<Collection<Rank>> getRanks(UUID uuid) {
        return this.rankResource.getRanks(uuid);
    }

    @Override
    public CompletableFuture<String> getPlayerId(UUID uuid) {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try(Connection connection = this.dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT sp.identifier FROM player p LEFT JOIN synced_players sp on p.id = sp.player_id JOIN bot b on sp.bot_id = b.id WHERE p.uuid = ? AND b.name = ?;")) {
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setString(2, this.bot.getName());
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    if(resultSet.next()) {
                        return resultSet.getString("identifier");
                    } else {
                        return null;
                    }
                }
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        });

        completableFuture.exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture<UUID> getUuid(String playerId) {
        CompletableFuture<UUID> completableFuture =  CompletableFuture.supplyAsync(() -> {
            try(Connection connection = this.dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT p.uuid FROM player p LEFT JOIN synced_players sp ON p.id = sp.player_id JOIN bot b ON sp.bot_id = b.id WHERE sp.identifier = ? AND sp.bot_id = (SELECT id FROM bot WHERE b.name = ?);")) {
                preparedStatement.setString(1, playerId);
                preparedStatement.setString(2, this.bot.getName());
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    if(resultSet.next()) {
                        return UUID.fromString(resultSet.getString("uuid"));
                    } else {
                        return null;
                    }
                }
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        });

        completableFuture.exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> setUuid(String playerId, UUID uuid) {
        return this.bot.getPlayerFactory().setUuid(playerId, uuid).thenAcceptAsync(a -> {
            if(uuid != null) {
                try(Connection connection = this.dataSource.getConnection();
                    PreparedStatement createPlayer = connection.prepareStatement("INSERT IGNORE player (uuid) VALUES (?);");
                    PreparedStatement saveUuid = connection.prepareStatement("INSERT INTO synced_players (bot_id, identifier, player_id) VALUES ((SELECT id FROM bot WHERE name = ?), ?, (SELECT id FROM player WHERE uuid = ?));")) {
                    createPlayer.setString(1, uuid.toString());
                    createPlayer.execute();

                    saveUuid.setString(1, this.bot.getName());
                    saveUuid.setString(2, playerId);
                    saveUuid.setString(3, uuid.toString());
                    saveUuid.execute();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            } else {
                try(Connection connection = this.dataSource.getConnection();
                    PreparedStatement unLink = connection.prepareStatement("DELETE FROM synced_players WHERE identifier = ? AND bot_id = (SELECT id FROM bot WHERE name = ?);")) {
                    unLink.setString(1, playerId);
                    unLink.setString(2, this.bot.getName());

                    unLink.execute();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }
}
