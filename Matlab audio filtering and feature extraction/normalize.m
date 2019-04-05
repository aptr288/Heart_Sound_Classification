function result=normalize( matrix )
% Normalizes the matrix, so that all the values will be
% included in the interval [0 1]
Nor = matrix - min( matrix(:));
result = Nor / max( Nor(:));
