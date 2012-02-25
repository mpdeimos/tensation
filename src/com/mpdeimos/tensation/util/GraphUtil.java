package com.mpdeimos.tensation.util;

import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnection;
import com.mpdeimos.tensation.model.TensorConnectionAnchor;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Utility Classes for Graphs
 * 
 * @author mpdeimos
 */
public class GraphUtil
{
	/** @returns the connected subgraphs based ona bfs algorithm */
	public static Set<Tupel<Set<TensorBase>, Set<TensorConnection>>> getConnectedSubgraphs(
			Set<TensorBase> tensors,
			Set<TensorConnection> connections)
	{
		Deque<TensorBase> u = new LinkedList<TensorBase>(tensors);
		Deque<TensorBase> v = new LinkedList<TensorBase>();
		Deque<TensorConnection> cs = new LinkedList<TensorConnection>(
				connections);
		Set<Tupel<Set<TensorBase>, Set<TensorConnection>>> retSet = new HashSet<Tupel<Set<TensorBase>, Set<TensorConnection>>>();
		while (!u.isEmpty())
		{
			v.push(u.pop());
			Tupel<Set<TensorBase>, Set<TensorConnection>> retPair = new Tupel<Set<TensorBase>, Set<TensorConnection>>();
			retPair.$1 = new HashSet<TensorBase>();
			retPair.$2 = new HashSet<TensorConnection>();

			while (!v.isEmpty())
			{
				TensorBase r = v.pop();
				retPair.$1.add(r);
				for (TensorConnection c : cs)
				{
					TensorBase other = null;
					if (r == c.getSink().getTensor())
						other = c.getSource().getTensor();
					else if (r == c.getSource().getTensor())
						other = c.getSink().getTensor();
					else
						continue;

					retPair.$2.add(c);

					if (retPair.$1.add(other))
					{
						u.remove(other); // also remove from u
						v.add(other);
					}
				}
			}

			retSet.add(retPair);
		}

		return retSet;
	}

	/**
	 * @return the complete subgraph a connection is part of.
	 */
	public static Tupel<Set<TensorBase>, Set<TensorConnection>> getCompleteSubgraph(
			TensorConnection connection)
	{
		return getCompleteSubgraph(connection.getSink().getTensor());
	}

	/**
	 * @return the complete subgraph a tensor is part of.
	 */
	public static Tupel<Set<TensorBase>, Set<TensorConnection>> getCompleteSubgraph(
			TensorBase tensor)
	{
		Deque<TensorBase> u = new LinkedList<TensorBase>();

		Set<TensorBase> tensors = new HashSet<TensorBase>();
		Set<TensorConnection> connections = new HashSet<TensorConnection>();

		u.add(tensor);
		tensors.add(tensor);

		while (!u.isEmpty())
		{
			TensorBase t = u.pop();

			for (TensorConnectionAnchor anchor : t.getOccupiedAnchors())
			{
				TensorConnection con = anchor.getConnection();
				connections.add(con);

				TensorBase tt = con.getSink().getTensor();

				if (!tensors.contains(tt))
				{
					tensors.add(tt);
					u.add(tt);
				}

				tt = con.getSource().getTensor();

				if (!tensors.contains(tt))
				{
					tensors.add(tt);
					u.add(tt);
				}

			}

		}

		return new Tupel<Set<TensorBase>, Set<TensorConnection>>(
				tensors,
				connections);
	}
}
