/*
 * Copyright (C) 2013 SeqWare
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.seqware.pipeline.plugins.checkdb.plugins;

import java.sql.SQLException;
import java.util.Set;
import java.util.SortedMap;
import net.sourceforge.seqware.pipeline.plugins.checkdb.CheckDBPluginInterface;
import net.sourceforge.seqware.pipeline.plugins.checkdb.SelectQueryRunner;
import org.openide.util.lookup.ServiceProvider;

/**
 * Checks the metadb for issues specifically with the sample hierarchy
 * @author dyuen
 */
@ServiceProvider(service=CheckDBPluginInterface.class)
public class SampleHierarchyPlugin implements CheckDBPluginInterface {

    @Override
    public void check(SelectQueryRunner qRunner, SortedMap<Level, Set<String>> result) throws SQLException {

        
        //List<Integer> executeQuery = qRunner.executeQuery("SELECT sw_accession FROM experiment WHERE experiment_id NOT IN (SELECT experiment_id FROM sample);", new ColumnListHandler<Integer>());
        //if (executeQuery.size() > 0) result.get(Level.TRIVIAL).add("Unreferenced Experiments: " + executeQuery.toString());
        
    }
    
}
